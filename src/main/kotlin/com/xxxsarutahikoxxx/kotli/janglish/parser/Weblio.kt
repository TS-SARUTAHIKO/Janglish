package com.xxxsarutahikoxxx.kotli.janglish.parser

import com.xxxsarutahikoxxx.kotli.janglish.*
import com.xxxsarutahikoxxx.kotli.janglish.structure.*
import com.xxxsarutahikoxxx.kotli.janglish.tag.TagLibrary
import com.xxxsarutahikoxxx.kotli.janglish.tag.VocabularyTag
import com.xxxsarutahikoxxx.kotlin.Span.ListSpan
import com.xxxsarutahikoxxx.kotlin.Span.TextSpan
import com.xxxsarutahikoxxx.kotlin.Utilitys.isSurrounded
import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import com.xxxsarutahikoxxx.kotlin.Utilitys.removeSpaceSurrounding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


object Weblio {
    fun getDocument(spell : String) : Document? {
        val document: Document = Jsoup.connect("https://ejje.weblio.jp/content/$spell").get()

        return if( document.getElementsByClass("intrst").isNotEmpty() ) document else null
    }

    fun parse(spell : String) : List<Vocabulary> {
        return if( " " !in spell ) parseVocabulary(spell) else parsePhrasal(spell)
    }
    private fun parsePhrasal(spell : String) : List<Vocabulary> {
        return listOf()
    }
    private fun parseVocabulary(spell: String) : List<Vocabulary> {
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

        // 音節を抽出する
        val syllable = (document.getElementsByClass("syllableEjje").firstOrNull()?.text() ?: "")
                .split("/")
                .map { it.replace("\\(米国英語\\)|\\(英国英語\\)".toRegex(), "").removeSpaceSurrounding }
                .filter { it.isNotBlank() && ! it.startsWith("-") && ! it.endsWith("-") }
                .distinct()

        // 発音記号を抽出する
        val phonetics = (document.getElementsByClass("phoneticEjjeWrp").firstOrNull()?.text() ?: "")
                .split("[｜/,]".toRegex())
                .map { it.replace("\\(米国英語\\)|\\(英国英語\\)".toRegex(), "").removeSpaceSurrounding }
                .filter { it.isNotBlank() && ! it.startsWith("-") && ! it.endsWith("-") }
                .distinct()

        // 研究社のブロックを抽出する
        val mainBlock = document.getElementsByClass("mainBlock hlt_KENEJ")?.first()

        // 記事要素を抽出する(１ページ内に複数のコンテンツが含まれる場合がある。fast -> [fast 1][fast 2] など)
        val articles = mainBlock?.getElementsByClass("midashigo")?.map { it.nextElementSibling() } ?: listOf()
        val sList = mutableListOf<Vocabulary>()

        // 記事要素をそれぞれ解析する
        for(it in articles){
            val ret = when {
                it.getElementsByClass("KnenjSub").isNotEmpty() -> parseVocabularyElement(it, level, conjugates, syllable, phonetics)
                // TODO : 句動詞の場合
                // TODO : 略語の場合
                // TODO : 接頭・接尾の場合
                // TODO : 上記の判定を作る
                else -> listOf()
            }

            // 作成した Vocabulary を格納する
            sList.addAll(ret)
        }

        return sList
    }

    private fun parseVocabularyElement(it : Element, level : Int?, conjugates : MutableMap<Conjugation, MutableList<String>>, bSyllable : List<String>, bPhonetics : List<String>) : List<Vocabulary> {
        val voc = ProtoVocabulary_Weblio()

        // Spell を設定する
        val spell = it.previousElementSibling().textNodes()[0].text()
        voc.spell = spell

        // Weblio Level を設定する
        level?.run{ voc.tagCodes.add(weblioLevel(level).code) }

        // 音節を抽出する
        val syllable = (it.getElementsByClass("KejjeOs").firstOrNull()?.text() ?: "")
                .split("/")
                .map { it.replace("\\(米国英語\\)|\\(英国英語\\)".toRegex(), "").removeSpaceSurrounding }
                .filter { it.isNotBlank() && ! it.startsWith("-") && ! it.endsWith("-") }
                .distinct()
        voc.syllable = setOf(*bSyllable.toTypedArray(), *syllable.toTypedArray()).toMutableList()

        // 発音記号を抽出する
        val phonetics = (it.getElementsByClass("KejjeHt").firstOrNull()?.text() ?: "")
                .split("[｜/,]".toRegex())
                .map { it.replace("\\(米国英語\\)|\\(英国英語\\)".toRegex(), "").removeSpaceSurrounding }
                .filter { it.isNotBlank() && ! it.startsWith("-") && ! it.endsWith("-") }
                .distinct()
        voc.phonetic = setOf(*bPhonetics.toTypedArray(), *phonetics.toTypedArray()).toMutableList()

        out = voc.phonetic

        // 発音リソースを抽出する
        val sources = it.getElementsByTag("source").map { it.attr("src") }
        voc.resource.add(Pronounce(spell, phonetics.firstOrNull()?:"", sources.firstOrNull()?:"").apply { isWeblio = true })


        // Level 0 or 例文 を抽出する
        var activeText = mutableMapOf<Int, String>()

        var activePart : PartMeaning? = null
        var activeFirst : FirstMeaning? = null
        var activeSecond : SecondMeaning? = null
        var activeExamples : MutableMap<String, String>? = null

        var activeFirstList = mutableListOf<FirstMeaning>()
        var activeSecondList = mutableListOf<SecondMeaning>()

        val defaultKey = "A"
        var aKey = defaultKey

        var partLevels_fromPart = mutableListOf<String>()


        fun isFirstEmpty() : Boolean {
            return activeFirstList.isEmpty()
        }
        fun flushSecond(){
            // ActiveText に Second-Level-Text が設定されているか、例文が存在する場合は Second-Level を構築して使用したデータは消去する
            if( activeExamples?.isNotEmpty() == true || activeText.containsKey(2) ){
                if( activeText.isEmpty() ){
                    out = "$activeFirstList : $activeSecondList"
                    out = "$activePart : $activeFirst : $activeSecond : $activeText : $activeExamples"
                }

                val (key, value) = activeText.entries.last()
                activeText.remove(key)

                if( activeSecond == null ) activeSecond = SecondMeaning()
                activeSecond?.apply {
                    secondLevel = value ?: ""
                    examples = activeExamples ?: mutableMapOf()

                    activeSecondList.add(this)
                    activeSecond = null
                }

                activeExamples = mutableMapOf()
            }
        }
        fun flushFirst(){
            flushSecond()

            if( activeSecondList.isNotEmpty() || activeText.containsKey(1) ){
                // First-Level テキストに Second-Level テキストが設定されている場合
                // 意味が一つで単純な場合に発生する (e.g. agreement)
                if( activeSecondList.isEmpty() && activeText.containsKey(1) ){
                    activeText[2] = activeText[1]!!
                    activeText.remove(1)
                    flushSecond()
                }

                // First-Level-Text を決定する
                val value = activeText[1]?.apply { activeText.remove(1) } ?: ""

                // 現在のデータから First-Level を作成してリストに保存する
                if( activeFirst == null ) activeFirst = FirstMeaning()
                activeFirst?.apply {
                    firstLevel = value

                    seconds = activeSecondList
                    activeSecondList = mutableListOf()

                    activeFirstList.add(this)
                    activeFirst = null
                }
            }
        }
        fun flushKey(){
            flushFirst()

            voc.meaningsMap.putIfAbsent(aKey, mutableListOf())

            if( activeFirstList.isNotEmpty() || activeText.containsKey(0) ){
                // Part-Level テキストに Second-Level テキストが設定されている場合
                // 意味が一つで単純な場合に発生する (e.g. asparagus)
                if( activeFirstList.isEmpty() && activeText.containsKey(0) ){
                    activeText[2] = activeText[0]!!
                    activeText.remove(0)
                    flushFirst()
                }

                // Part-Level-Text を決定する
                val value = activeText[0]?.apply { activeText.remove(0) } ?: ""

                activePart?.apply {
                    partLevel = partLevels_fromPart.append(value)

                    firsts = activeFirstList
                    activeFirstList = mutableListOf()

                    voc.meaningsMap[aKey]?.add(this)
                    activePart = PartMeaning(activePart!!.part)
                }
            }
        }
        fun flushPart(){
            flushKey()

            partLevels_fromPart = mutableListOf()
        }

        val levels = it.children().filter { it.className() in listOf("level0", "KejjeYr") }
        levels.forEach {
            when {
                // 品詞の更新
                it.getElementsByClass("KnenjSub").isNotEmpty() -> {
                    flushPart()

                    val partText = it.getElementsByClass("KnenjSub").text()
                    val spans = ListSpan.parse(partText).spans

                    // 品詞名
                    val part = spans[0].toString().removeSpaceSurrounding
                    // 品詞の [] などで括られた部分を切り出す
                    val suffix = spans.subList(1, spans.size).joinToString("")

                    // Active の切り替え
                    activePart = PartMeaning(PartOfSpeech.of(part))
                    aKey = defaultKey
                    // Part-Level-Text の保存
                    partLevels_fromPart.add(suffix)
                }
                // キーの切り替え
                it.getElementsByClass("lvlUAH").isNotEmpty() -> {
                    flushKey()

                    val key = it.getElementsByClass("lvlUAH").first().text()
                    val levelText = it.getElementsByClass("lvlUAB").firstOrNull()?.text() ?: ""

                    // Active の切り替え
                    aKey = key

                    if( levelText.isNotBlank() ) activeText[0] = levelText
                }
                else -> {
                    val fIndex = it.getElementsByClass("lvlNH").firstOrNull()?.text() ?: ""
                    val sIndex = it.getElementsByClass("lvlAH").firstOrNull()?.text() ?: ""
                    val levelText = it.getElementsByClass("lvlB").firstOrNull()?.text() ?: ""

                    val english = it.getElementsByClass("KejjeYrEn").map { it.text()?:"" }
                    val japanese = it.getElementsByClass("KejjeYrJp").map { it.text()?:"" }
                    val translated = english.zip(japanese).toMap().toMutableMap()

                    // ※ 例文枠を用いているが英/日形式になっていない場合がある。その場合用の判定として it.className() != "KejjeYr" を用いる

                    when {
                        // 例文を追加する
                        it.className() == "KejjeYr" -> {
                            if( activeExamples == null ) activeExamples = mutableMapOf()

                            if( translated.isEmpty() ) translated[it.text()] = "NaN"

                            activeExamples?.putAll(translated)
                        }

                        // 全てない場合
                        fIndex.isEmpty() && sIndex.isEmpty() && levelText.isEmpty() -> {
                            when {
                                // [prefix- + xxx] [xxx + -suffix] の形式で由来が表されている場合
                                ( it.text().removeSpaceSurrounding.isSurrounded("［", "］") ) -> {
                                    // TODO : 由来を単語レベルで保存する
                                }
                                // オーディオタグが存在する場合
                                ( it.getElementsByTag("audio").isNotEmpty() ) -> {
                                    // TODO : 発音リソースの読み込み
                                }

                                // 形容詞か副詞の PartLevel-Text で比較級・最上級の情報である場合
                                ((activePart?.part?.isAdjective == true) || (activePart?.part?.isAdverb == true)) && isFirstEmpty() && it.text().startsWith("(") -> {
                                    val spans = ListSpan.parse(it.text(), ListSpan.Decorations + ('/' to '/')).spans
                                    val conj = spans[0]
                                    val rest = spans.subList(1, spans.size)

                                    // 比較級・最上級を取り除いた部分を Part-Level-Text として設定する
                                    rest.joinToString("").let {
                                        if( it.isNotBlank() ) activeText[0] = it
                                    }

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
                                            val sufOfS = s.split("・").last()
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
                                }


                                // Part-Level-Text が既に存在する場合は上書きが起きないようにする
                                ( activeText.containsKey(0) ) -> {
                                    // TODO :
                                }
                                else -> {
                                    // Part-Level のテキストの場合
                                    activeText[0] = it.text()
                                }
                            }
                        }
                        // First-Index / Second-Index / Level-Text がある場合
                        fIndex.isNotEmpty() && sIndex.isNotEmpty() && levelText.isNotEmpty() -> {
                            flushFirst()

                            activeFirst = FirstMeaning()
                            activeSecond = SecondMeaning()
                            activeText[0] = levelText
                        }
                        // First-Index / Level-Text がある場合
                        fIndex.isNotEmpty() && sIndex.isEmpty() && levelText.isNotEmpty() -> {
                            flushFirst()

                            activeFirst = FirstMeaning()
                            activeText[1] = levelText
                        }
                        // Second-Index / Level-Text がある場合
                        fIndex.isEmpty() && sIndex.isNotEmpty() && levelText.isNotEmpty() -> {
                            flushSecond()

                            activeSecond = SecondMeaning()
                            activeText[2] = levelText
                        }
                        else -> {
                            out = "Error : Not Match"
                        }
                    }
                }
            }
        }
        flushPart()

        return voc.toReal.map {
            // 動詞の意味を持つなら動詞の活用を取り込む
            if( PartOfSpeech.Verb in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isVerb }.toMutableMap())
            // 名詞の意味を持つなら名詞の活用を取り込む
            if( PartOfSpeech.Noun in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isNoun }.toMutableMap())
            // 形容詞の意味を持つなら形容詞の活用を取り込む
            if( PartOfSpeech.Adjective in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isAdjective }.toMutableMap())
            // 副詞の意味を持つなら副詞の活用を取り込む
            if( PartOfSpeech.Adverb in it.majorParts ) it.conjugations.putAll(conjugates.filterKeys { it.isAdverb }.toMutableMap())

            it
        }
    }

    fun weblioLevel(level : Int) : VocabularyTag {
        return TagLibrary.instance("Weblio Lv.$level", "Weblio")
    }
}


// Weblio のページ解析の途中で用いる中継クラス
internal class ProtoVocabulary_Weblio() {
    /** スペル */
    var spell : String = ""
    /** コア */
    var summary : MutableMap<PartOfSpeech, String> = mutableMapOf()
    /** 活用形 */
    var conjugations : MutableMap<Conjugation, MutableList<String>> = mutableMapOf()
    /** 音節 */
    var syllable : MutableList<String> = mutableListOf()
    /** 発音記号 */
    var phonetic : MutableList<String> = mutableListOf()
    /** 発音リソース */
    var resource : MutableList<Pronounce> = mutableListOf()

    /** タグ */
    var tagCodes : MutableList<String> = mutableListOf()

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
                tagCodes = tagCodes.toMutableList(),
                phrasal = phrasal.toMutableMap(),
                synonyms = synonyms.toMutableMap(),
                antonyms = antonyms.toMutableMap(),
                related = related.toMutableMap(),
                meanings = it
            ).apply {
                // 特定の文字列を括る関数
                fun String.reform() : String {
                    return this.replace("^可算名詞".toRegex(), "【可算名詞】")
                               .replace(" 可算名詞 ".toRegex(), " 【可算名詞】 ")
                               .replace("^不可算名詞".toRegex(), "【不可算名詞】")
                               .replace(" 不可算名詞 ".toRegex(), " 【不可算名詞】 ")
                               .replace("^限定用法の形容詞".toRegex(), "【限定用法の形容詞】")
                               .replace(" 限定用法の形容詞 ".toRegex(), " 【限定用法の形容詞】 ")
                               .replace("^叙述的用法の形容詞".toRegex(), "【叙述的用法の形容詞】")
                               .replace(" 叙述的用法の形容詞 ".toRegex(), " 【叙述的用法の形容詞】 ")
                }

                // First-Level のテキストを調整する
                allFirstLevel.forEach {
                    it.firstLevel = it.firstLevel.reform()
                }
                // Second-Level のテキストを調整する
                allSecondLevel.forEach {
                    it.secondLevel = it.secondLevel.reform()

                    // 並び替え・セパレーター文字の統一
                    val (texts, lists) = ListSpan.parse(it.secondLevel).spans.partition { it is TextSpan }
                    val text = texts.joinToString(" ")
                        .split("[,.:;、， ]".toRegex())
                        .filter { it.isNotBlank() }
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
                    it.partLevel = it.partLevel.split("\n").append(list.joinToString("\n"))
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
