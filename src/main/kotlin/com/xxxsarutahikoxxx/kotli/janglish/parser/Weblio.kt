package com.xxxsarutahikoxxx.kotli.janglish.parser

import com.xxxsarutahikoxxx.kotli.janglish.out
import com.xxxsarutahikoxxx.kotli.janglish.structure.*
import com.xxxsarutahikoxxx.kotli.janglish.structure.ProtoVocabulary_Weblio
import com.xxxsarutahikoxxx.kotli.janglish.tag.VocabularyTag
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


object Weblio {
    fun getDocument(spell : String) : Document? {
        val document: Document = Jsoup.connect("https://ejje.weblio.jp/content/$spell").get()

        return if( document.getElementsByClass("intrst").isNotEmpty() ) document else null
    }

    fun parse(spell: String) : List<Vocabulary> {
        val document: Document = getDocument(spell) ?: return listOf()

        // 活用を抽出する
        val conjugates = mutableMapOf<Conjugation, String>()
        document.getElementsByClass("conjugateRowR").forEach {
            val a = it.getElementsByTag("a").map { it.text() }
            val span = it.getElementsByTag("span")
                    .map { it.text().removeSurrounding("(", ")") }
                    .map { Conjugation.of(it) }

            conjugates.putAll(span.zip(a).toMap())
        }

        // レベルを抽出する
        val level = document.getElementsByClass("learning-level-content").firstOrNull()?.text()?.run { Integer.parseInt(this) }

        // 研究社のブロックを抽出する
        val mainBlock = document.getElementsByClass("mainBlock hlt_KENEJ")?.first()

        // 記事要素を抽出する(１ページ内に複数のコンテンツが含まれる場合がある。fast -> [fast 1][fast 2] など)
        val articles = mainBlock?.getElementsByClass("midashigo")?.map { it.nextElementSibling() } ?: listOf()
        val ret = mutableListOf<Vocabulary>()

        // 記事要素をそれぞれ解析する
        articles.forEach {
            val defaultKey = "A"
            val voc = ProtoVocabulary_Weblio(spell)

            // Weblio Level を設定する
            level?.run{ voc.tags.add(weblioLevel(level).name) }

            // 音節を抽出する
            val syllable = (it.getElementsByClass("KejjeOs").firstOrNull()?.text() ?: "")
                    .split("/")
            voc.syllable = syllable.toMutableList()

            // 発音記号を抽出する
            val phonetics = (it.getElementsByClass("KejjeHt").firstOrNull()?.text() ?: "")
                    .split("[｜/]".toRegex())
                    .filter { it.isNotBlank() }
            voc.phonetic = phonetics.toMutableList()

            // 発音リソースを抽出する
            val sources = it.getElementsByTag("source").map { it.attr("src") }
            voc.resource.addAll(sources)


            // Level 0 or 例文 を抽出する
            val levels = it.children().filter { it.className() in listOf("level0", "KejjeYr") }
            levels.forEach {
                when {
                    // 品詞の更新
                    it.getElementsByClass("KnenjSub").isNotEmpty() -> {
                        val part = it.getElementsByClass("KnenjSub").text().run { PartOfSpeech.of(this) }
                        voc.active = defaultKey to part
                    }
                    // コンテナーの更新・切り替え
                    it.getElementsByClass("lvlUAH").isNotEmpty() -> {
                        val key = it.getElementsByClass("lvlUAH").first().text()
                        voc.active = key to voc.activePart
                    }
                    else -> {
                        val first = it.getElementsByClass("lvlNH").firstOrNull()?.text() ?: ""
                        val second = it.getElementsByClass("lvlAH").firstOrNull()?.text() ?: ""
                        val meaning = it.getElementsByClass("lvlB").firstOrNull()?.text() ?: ""

                        val english = it.getElementsByClass("KejjeYrEn").map { it.text()?:"" }
                        val japanese = it.getElementsByClass("KejjeYrJp").map { it.text()?:"" }
                        val translated = english.zip(japanese).toMap()


                        //out = "$first : $second : $meaning : $translated"

                        when {
                            // 全てない場合
                            first.isEmpty() && second.isEmpty() && meaning.isEmpty() && translated.isEmpty() -> {
                                if( it.getElementsByClass("KejjeOs").size == 2 ){
                                    // 形容詞・副詞の比較級・最上級の場合１
                                    val comparatives = it.getElementsByClass("KejjeOs").map { it.text().replace("・", "") }

                                    conjugates[Conjugation.Comparative] = comparatives[0]
                                    conjugates[Conjugation.Superlative] = comparatives[1]
                                }else
                                if( it.textNodes().map { it.text() }.run { (size == 3) && (this[0] == "(") && (this[1] == "; ") && (this[2] == ")") } ){
                                    // 形容詞・副詞の比較級・最上級の場合２
                                    val comparatives = it.getElementsByTag("a").map { it.text().replace("・", "") }

                                    conjugates[Conjugation.Comparative] = comparatives[0]
                                    conjugates[Conjugation.Superlative] = comparatives[1]
                                }else
                                if( voc.activeMeaning.isFirstEmpty ){
                                    // Part-Level のテキストの場合
                                    voc.activeMeaning.partLevel = it.text()
                                }
                            }
                            // First / Second / Meaning がある場合
                            first.isNotEmpty() && second.isNotEmpty() && meaning.isNotEmpty() -> {
                                voc.activeMeaning.addFirstLevel("")
                                voc.activeMeaning.addSecondLevel(meaning)
                            }
                            // First / Meaning がある場合
                            first.isNotEmpty() && second.isEmpty() && meaning.isNotEmpty() -> {
                                voc.activeMeaning.addFirstLevel(meaning)
                            }
                            // Second / Meaning がある場合
                            first.isEmpty() && second.isNotEmpty() && meaning.isNotEmpty() -> {
                                voc.activeMeaning.addSecondLevel(meaning)
                            }
                            // 例文を追加する
                            translated.isNotEmpty() -> {
                                voc.activeMeaning.addTranslated( translated )
                            }
                            else -> {
                                out = "Error : Not Match"
                            }
                        }
                    }
                }
            }

            // 作成した Vocabulary を格納する
            ret.addAll(voc.toReal.map {
                // 動詞の意味を持つなら動詞の活用を取り込む
                if( PartOfSpeech.Verb in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isVerb }.toMutableMap())
                // 名詞の意味を持つなら名詞の活用を取り込む
                if( PartOfSpeech.Noun in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isNoun }.toMutableMap())
                // 形容詞の意味を持つなら形容詞の活用を取り込む
                if( PartOfSpeech.Adjective in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isAdjective }.toMutableMap())
                // 副詞の意味を持つなら副詞の活用を取り込む
                if( PartOfSpeech.Adverb in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isAdverb }.toMutableMap())

                it
            })
        }

        return ret
    }


    private val weblioTag = VocabularyTag.of("Weblio", "Classifier")
    fun weblioLevel(level : Int) : VocabularyTag {
        val name = "Weblio Lv.$level"

        return VocabularyTag.of(name, "Weblio")
    }
}

