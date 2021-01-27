package com.xxxsarutahikoxxx.kotli.janglish.parser

import com.xxxsarutahikoxxx.kotli.janglish.*
import com.xxxsarutahikoxxx.kotli.janglish.structure.*
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
        val conjugates = mutableMapOf<Conjugation, MutableList<String>>()
        document.getElementsByClass("conjugateRowR").forEach {
            val a = it.getElementsByTag("a").map { it.text() }
            val span = it.getElementsByTag("span")
                    .map { it.text().removeSurrounding("(", ")") }
                    .map { Conjugation.of(it) }

            span.zip(a).forEach {
                conjugates.put(it.first, mutableListOf(it.second))
            }
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
                    .split("[｜/,]".toRegex())
                    .map { it.removeSpaceSurrounding }
                    .filter { it.isNotBlank() }
                    .distinct()
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
                                // 形容詞か副詞の PartLevel-Text で比較級・最上級の情報である場合
                                if( (voc.activePart.isAdjective || voc.activePart.isAdverb) && voc.activeMeaning.isFirstEmpty && it.text().startsWith("(") ){
                                    val (conj : Span, rest : List<Span> ) = ListSpan.parse(it.text(), ListSpan.Decorations.toMutableMap().apply { put('/', '/') }).spans.run { this[0] to this.subList(1, this.size) }

                                    // 比較級・最上級を取り除いた部分を設定する
                                    voc.activeMeaning.partLevel = rest.joinToString("")

                                    // 活用部分の文字列を切り出す
                                    var text = (conj as ListSpan).spans.filterIsInstance(TextSpan::class.java).joinToString("")
                                    text = text.replace("音節|発音記号・読み方".toRegex(), "")
                                    var (base1 : List<String>, base2 : List<String>) = text.split(";")
                                        .map { it.removeSpaceSurrounding }
                                        .filter { "[a-zA-Z‐]".toRegex().matches("${it.getOrNull(0)?:'+'}") }
                                        .run { (this.getOrNull(0)?:"") to (this.getOrNull(1)?:"") }
                                        .run {
                                            this.first.split("[,，]".toRegex()).filter { it.isNotBlank() }.map { it.removeSpaceSurrounding } to this.second.split("[,，]".toRegex()).filter { it.isNotBlank() }.map { it.removeSpaceSurrounding }
                                        }
                                    var (c1, c2) = base1 to base2

                                    //
                                    val cList = mutableListOf<String>()
                                    val sList = mutableListOf<String>()


                                    // more / most の切り出しと保存
                                    c1.filter { it.startsWith("more ") }.apply {
                                        cList.addAll(this) ; c1 = c1 - this
                                    }
                                    c2.filter { it.startsWith("more ") }.apply {
                                        cList?.addAll(this) ; c2 = c2 - this
                                    }
                                    c1.filter { it.startsWith("most ") }.apply {
                                        sList?.addAll(this) ; c1 = c1 - this
                                    }
                                    c2.filter { it.startsWith("most ") }.apply {
                                        sList?.addAll(this) ; c2 = c2 - this
                                    }


                                    // 比較級と最上級のリスト作成
                                    var (comparative : List<String>, superlative : List<String>) = when {
                                        c1.isNotEmpty() && c2.isEmpty() -> listOf( c1[0] ) to listOf( c1[1] )
                                        c1.isEmpty() && c2.isNotEmpty() -> listOf( c2[0] ) to listOf( c2[1] )
                                        else -> c1 to c2
                                    }


                                    // 調整の必要がないものを切り出して保存する
                                    comparative.filter { ! it.startsWith("‐") && ! it.contains("・") }.apply {
                                        cList.addAll(this) ; comparative = comparative - this
                                    }
                                    superlative.filter { ! it.startsWith("‐") && ! it.contains("・") }.apply {
                                        sList.addAll(this) ; superlative = superlative - this
                                    }


                                    // 最上級が ‐ から始まってる場合は比較級から合成する
                                    superlative = superlative.mapIndexed { index, s ->
                                        if( s.startsWith("‐") ){
                                            val preOfC = comparative[index].split("・").run { subList(0, this.size-1) }
                                            val sufOfS = s.substring(1, s.length)
                                            listOf(*preOfC.toTypedArray(), sufOfS).joinToString("")
                                        }else{
                                            s.replace("・", "")
                                        }
                                    }
                                    comparative = comparative.map { it.replace("・", "") }


                                    // 元の順序基準で並び替えて保存する
                                    conjugates.putIfAbsent(Conjugation.Comparative, mutableListOf())
                                    conjugates.putIfAbsent(Conjugation.Superlative, mutableListOf())

                                    conjugates[Conjugation.Comparative]?.addAll(listOf(
                                        *cList.toTypedArray(), *comparative.toTypedArray()
                                    ).sortedBy {
                                        listOf(*base1.toTypedArray(), *base2.toTypedArray()).indexOf(it)
                                    })
                                    conjugates[Conjugation.Superlative]?.addAll(listOf(
                                            *sList.toTypedArray(), *superlative.toTypedArray()
                                    ).sortedBy {
                                        listOf(*base1.toTypedArray(), *base2.toTypedArray()).indexOf(it)
                                    })

                                    // 重複を除去する
                                    conjugates[Conjugation.Comparative] = conjugates[Conjugation.Comparative]!!.distinct().toMutableList()
                                    conjugates[Conjugation.Superlative] = conjugates[Conjugation.Superlative]!!.distinct().toMutableList()
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


// Weblio のページ解析の途中で用いる中継クラス
internal class ProtoVocabulary_Weblio(spell : String) {
    /** スペル */
    var spell : String = spell
    /** コア */
    var summary : MutableMap<PartOfSpeech, String> = mutableMapOf()
    /** 活用形 */
    var conjugations : MutableMap<Conjugation, MutableList<String>> = mutableMapOf()
    /** 音節 */
    var syllable : MutableList<String> = mutableListOf()
    /** 発音記号 */
    var phonetic : MutableList<String> = mutableListOf()
    /** 発音リソース */
    var resource : MutableList<String> = mutableListOf()

    /** タグ */
    var tags : MutableList<String> = mutableListOf()

    /** 句動詞 */
    var phrasal : MutableMap<String, String> = mutableMapOf()
    /** 類義語 */
    var synonyms : MutableMap<String, MutableMap<String, String>> = mutableMapOf()
    /** 対義語 */
    var antonyms : MutableMap<String, MutableMap<String, String>> = mutableMapOf()
    /** 関連語 */
    var related : MutableMap<String, String> = mutableMapOf()

    /**
     * 意味マップ
     *
     * 意味が大別される２系統に分かれている場合用
     * */
    var meaningsMap : MutableMap<String, MutableList<PartMeaning>> = mutableMapOf()

    var active : Pair<String, PartOfSpeech> = "" to PartOfSpeech.Others
        set(value) {
            meaningsMap.putIfAbsent(value.first, mutableListOf())

            if( meaningsMap[value.first]!!.filter { it.part == value.second }.isEmpty() ){
                meaningsMap[value.first]!!.add(PartMeaning(part = value.second).apply {
                    if( activeKey != value.first && activePart == value.second ){
                        partLevel = activeMeaning.partLevel
                    }
                })
            }

            field = value
        }
    val activeKey : String get() = active.first
    val activePart : PartOfSpeech get() = active.second
    val activeMeaning : PartMeaning get() = meaningsMap[activeKey]!!.first { it.part == activePart }


    /** [ProtoVocabulary_Weblio] -> [Vocabulary] への変換 */
    val toReal : List<Vocabulary> get(){
        return meaningsMap.values.map {
            Vocabulary(
                spell = spell,
                summary = summary.toMutableMap(),
                conjugations = conjugations.toMutableMap(),
                syllable = syllable.toMutableList(),
                phonetic = phonetic.toMutableList(),
                resource = resource.toMutableList(),
                tags = tags.toMutableList(),
                phrasal = phrasal.toMutableMap(),
                synonyms = synonyms.toMutableMap(),
                antonyms = antonyms.toMutableMap(),
                related = related.toMutableMap(),
                meanings = it
            ).apply {
                allPartLevel.forEach { it.adjust() }

                // First-Level のテキストを調整する
                allFirstLevel.forEach {
                    // 特定の名詞を括る
                    it.firstLevel = it.firstLevel
                        .replace("^可算名詞".toRegex(), "【可算名詞】")
                        .replace("^不可算名詞".toRegex(), "【不可算名詞】")
                        .replace("^限定用法の形容詞".toRegex(), "【限定用法の形容詞】")
                        .replace("^叙述的用法の形容詞".toRegex(), "【叙述的用法の形容詞】")
                }
                // Second-Level のテキストを調整する
                allSecondLevel.forEach {
                    // 特定の名詞を括る
                    it.secondLevel = it.secondLevel
                        .replace("^可算名詞".toRegex(), "【可算名詞】")
                        .replace("^不可算名詞".toRegex(), "【不可算名詞】")
                        .replace("^限定用法の形容詞".toRegex(), "【限定用法の形容詞】")
                        .replace("^叙述的用法の形容詞".toRegex(), "【叙述的用法の形容詞】")

                    // 並び替え・セパレーター文字の統一
                    val (texts, lists) = ListSpan.parse(it.secondLevel).spans.partition { it is TextSpan }
                    val text =  texts.joinToString(" ")
                        .split("[,.:;、，]".toRegex())
                        .filter { it.isNotBlank() }
                        .map { it.removeSpaceSurrounding }
                        .distinct()
                        .joinToString(", ")
                    val list = lists.joinToString("")

                    it.secondLevel = "$text $list"
                }

                // 【解説】, 【類語】を切り出して移動する
                val tag = listOf("《★【解説】", "《★【類語】")
                meanings.forEach {
                    val list = mutableListOf<String>()
                    it.firsts.forEach {
                        val fLevel = it.firstLevel
                        if( tag.any { it in fLevel } ){
                            val (rest, main) = ListSpan.parse(it.firstLevel).spans.partition { it is ListSpan && it.open == '《' && it.toString().startsWith("《★") }
                            it.firstLevel = main.joinToString("")
                            list.add(rest.joinToString(""))
                        }

                        it.seconds.forEach {
                            val sLevel = it.secondLevel
                            if( tag.any { it in sLevel } ){
                                val (rest, main) = ListSpan.parse(it.secondLevel).spans.partition { it is ListSpan && it.open == '《' && it.toString().startsWith("《★") }
                                it.secondLevel = main.joinToString("")
                                list.add(rest.joinToString(""))
                            }
                        }
                    }
                    it.partLevel = listOf<String>(it.partLevel, *list.toTypedArray()).filter { it.isNotBlank() }.joinToString("\n")
                }

                // 概要の自動作成
                val map = mutableMapOf<PartOfSpeech, List<String>>()
                meanings.forEachIndexed { pIndex, partMeaning ->
                    var mapInPart = mutableListOf<Pair<String, Triple<Int, Int, Int>>>()

                    // ラベリングを行う
                    partMeaning.firsts.forEachIndexed { fIndex, firstMeaning ->
                        firstMeaning.seconds.forEachIndexed { sIndex, secondMeaning ->
                            ListSpan.parse(secondMeaning.secondLevel).spans.filterIsInstance(TextSpan::class.java).firstOrNull()?.apply {
                                val texts = this.toString().split(", ")
                                texts.forEachIndexed { index, s ->
                                    mapInPart.add( s.removeSpaceSurrounding to Triple(fIndex, sIndex, index) )
                                }
                            }
                        }
                    }

                    // 重複の削除・並び替える・切り出し・並び替えを行う
                    mapInPart = mapInPart.distinctBy { it.first }.toMutableList()
                    mapInPart.sortBy { val (f, s, n) = it.second ; n*100+s*10+f }
                    mapInPart = mapInPart.subList(0, minOf(mapInPart.size, 4)).toMutableList()
                    mapInPart.sortBy { val (f, s, n) = it.second ; f*100+s*10+n }

                    // 作成された概要を確保する
                    map[partMeaning.part] = mapInPart.map { it.first }
                }

                majorParts.filter{ ! it.isSpecial }.forEach {
                    val major = it
                    val parts = map.keys.filter { it in major }

                    summary[major] = when( parts.size ){
                        1 -> parts.map { map[it]!!.run { subList(0, minOf(4, this.size)) }.joinToString(", ") }.joinToString(" / ")
                        2 -> parts.map { map[it]!!.run { subList(0, minOf(2, this.size)) }.joinToString(", ") }.joinToString(" / ")
                        else -> parts.map{ map[it]!!.run { subList(0, minOf(1, this.size)) }.joinToString(", ") }.joinToString(" / ")
                    }
                }

                summary[PartOfSpeech.Overview] = summary.values.joinToString(" / ")
            }
        }
    }
}

internal fun PartMeaning.addFirstLevel( first : String ){
    if( ! isFirstEmpty && isSecondEmpty && activeFirstLevel.isNotBlank() ){
        addSecondLevel(activeFirstLevel)
        firsts.last().firstLevel = ""
    }

    firsts.add(FirstMeaning(first))
}
internal fun PartMeaning.addSecondLevel( meaning : String ){
    firsts.last().seconds.add(SecondMeaning(meaning))
}
internal fun PartMeaning.addTranslated( translated : Map<String, String> ){
    if( isFirstEmpty ){
        addFirstLevel("")
        addSecondLevel(partLevel)
        partLevel = ""
    }else
        if( isSecondEmpty ){
            addSecondLevel(activeFirstLevel)
            firsts.last().firstLevel = ""
        }

    firsts.last().seconds.last().examples.putAll(translated)
}

internal fun PartMeaning.adjust(){
    when {
        partLevel.isNotBlank() && isFirstEmpty -> {
            addFirstLevel("")
            addSecondLevel(partLevel)
            partLevel = ""
        }
    }
    firsts.forEach {
        when {
            it.firstLevel.isNotBlank() && it.seconds.isEmpty() -> {
                addSecondLevel(it.firstLevel)
                it.firstLevel = ""
            }
        }
    }
}