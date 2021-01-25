package com.xxxsarutahikoxxx.kotli.janglish.structure

import com.xxxsarutahikoxxx.kotli.janglish.ListSpan
import com.xxxsarutahikoxxx.kotli.janglish.TextSpan
import com.xxxsarutahikoxxx.kotli.janglish.out
import kotlinx.serialization.Serializable

@Serializable
data class Vocabulary(
    /** スペル */
    var spell : String,
    /** コア */
    var summary : MutableMap<PartOfSpeech, String>,
    /** 活用形 */
    var conjugations : MutableMap<Conjugation, String>,
    /** 音節 */
    var syllable : MutableList<String>,
    /** 発音記号 */
    var phonetic : MutableList<String>,
    /** 発音リソース */
    var resource : MutableList<String>,

    /** タグ */
    var tags : MutableList<String>,

    /** 句動詞 */
    var phrasal : MutableMap<String, String>,
    /** 類義語 */
    var synonyms : MutableMap<String, MutableMap<String, String>>,
    /** 対義語 */
    var antonyms : MutableMap<String, MutableMap<String, String>>,
    /** 関連語 */
    var related : MutableMap<String, String>,

    /** 意味 */
    var meanings : MutableList<PartMeaning>,

    /** 評価値 */
    var evaluation  : Float = 3.0f
){
    /** 品詞のリスト */
    val parts : List<PartOfSpeech> get() = meanings.map { it.part }
    /** トップレベルの品詞のリスト */
    val majorParts : List<PartOfSpeech> get() = parts.map { it.parent }.distinct()

    /** 活用形の存在 */
    fun has(con : Conjugation) : Boolean = conjugations.containsKey(con)
    /** 過去形を持つかどうか */
    val hasPastForm get() = has(Conjugation.Past)
    /** 過去分詞を持つかどうか */
    val hasPastParticipleForm get() = has(Conjugation.PastParticiple)
    /** 現在分詞を持つかどうか */
    val hasPresentParticipleForm get() = has(Conjugation.PresentParticiple)
    /** 複数形を持つかどうか */
    val hasPluralForm get() = has(Conjugation.Plural)
    /** 三人称単数形を持つかどうか */
    val hasThirdPersonSingularForm get() = has(Conjugation.ThirdPersonSingular)

    /** 活用の形を取得する */
    fun form(con : Conjugation) = conjugations[con]
    /** 過去の形を取得する */
    val formOfPast get() = form(Conjugation.Past)
    /** 過去分詞の形を取得する */
    val formOfPastParticiple get() = form(Conjugation.PastParticiple)
    /** 現在分詞の形を取得する */
    val formOfPresentParticiple get() = form(Conjugation.PresentParticiple)
    /** 複数の形を取得する */
    val formOfPlural get() = form(Conjugation.Plural)
    /** 三人称単数の形を取得する */
    val formOfThirdPersonSingular get() = form(Conjugation.ThirdPersonSingular)


    /** [part]に属する[FirstMeaning]のリスト */
    fun meaningsOfMajor(part : PartOfSpeech) : List<FirstMeaning> {
        val part = part.parent
        return meanings.filter { it.part in part }.map { it.meanings }.flatten()
    }
}
@Serializable
data class PartMeaning(
    var part : PartOfSpeech,
    var partLevel : String = "",
    var meanings : MutableList<FirstMeaning> = mutableListOf()
)
@Serializable
data class FirstMeaning(
    var firstLevel : String = "",
    var meanings : MutableList<SecondMeaning> = mutableListOf()
)
@Serializable
data class SecondMeaning(
    var secondLevel : String = "",
    var examples : MutableMap<String /* English */, String /* Japanese */> = mutableMapOf()
)

// Weblio のページ解析の途中で用いる中継クラス
internal class ProtoVocabulary_Weblio(spell : String) {
    /** スペル */
    var spell : String = spell
    /** コア */
    var summary : MutableMap<PartOfSpeech, String> = mutableMapOf()
    /** 活用形 */
    var conjugations : MutableMap<Conjugation, String> = mutableMapOf()
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
            Vocabulary(spell = spell, summary = summary, conjugations = conjugations, syllable = syllable, phonetic = phonetic,
                    resource = resource, tags = tags, phrasal = phrasal, synonyms = synonyms, antonyms = antonyms, related = related,
                    meanings = it
            ).apply {
                // First-Level のテキストを調整する
                allFirstLevel.forEach {
                    // 特定の名詞を括る
                    it.firstLevel = it.firstLevel
                            .replace("^可算名詞".toRegex(), "【可算名詞】")
                            .replace("^不可算名詞".toRegex(), "【不可算名詞】")
                }
                // Second-Level のテキストを調整する
                allSecondLevel.forEach {
                    // 特定の名詞を括る
                    it.secondLevel = it.secondLevel
                            .replace("^可算名詞".toRegex(), "【可算名詞】")
                            .replace("^不可算名詞".toRegex(), "【不可算名詞】")

                    // 並び替え・セパレーター文字の統一
                    val (texts, lists) = ListSpan.parse(it.secondLevel).spans.partition { it is TextSpan }
                    val text =  texts.joinToString(" ")
                            .split("[,.:;、，]".toRegex())
                            .filter { it.isNotBlank() }
                            .map { it.replace("^ +".toRegex(), "").replace(" +$".toRegex(), "") }
                            .joinToString(", ")
                    val list = lists.joinToString("")

                    it.secondLevel = "$text $list"
                }

                // 【解説】, 【類語】を切り出して移動する
                val tag = listOf("《★【解説】", "《★【類語】")
                meanings.forEach {
                    val list = mutableListOf<String>()
                    it.meanings.forEach {
                        val fLevel = it.firstLevel
                        if( tag.any { it in fLevel } ){
                            val (rest, main) = ListSpan.parse(it.firstLevel).spans.partition { it is ListSpan && it.open == '《' }
                            it.firstLevel = main.joinToString("")
                            list.add(rest.joinToString(""))
                        }

                        it.meanings.forEach {
                            val sLevel = it.secondLevel
                            if( tag.any { it in sLevel } ){
                                val (rest, main) = ListSpan.parse(it.secondLevel).spans.partition { it is ListSpan && it.open == '《' }
                                it.secondLevel = main.joinToString("")
                                list.add(rest.joinToString(""))
                            }
                        }
                    }
                    it.partLevel = listOf<String>(it.partLevel, *list.toTypedArray()).filter { it.isNotBlank() }.joinToString("\n")
                }

                // 概要の自動作成

            }
        }
    }
}


// Vocabulary
val Vocabulary.allFirstLevel get() = meanings.map { it.meanings }.flatten()
val Vocabulary.allSecondLevel get() = allFirstLevel.map { it.meanings }.flatten()

// PartMeaning
val PartMeaning.isFirstEmpty get() = meanings.isEmpty()
val PartMeaning.isSecondEmpty get() = meanings.last().meanings.isEmpty()

val PartMeaning.activeFirstLevel : String get() = meanings.last().firstLevel
val PartMeaning.activeSecondLevel : String get() = meanings.last().meanings.last().secondLevel

fun PartMeaning.addFirstLevel( first : String ){
    if( ! isFirstEmpty && isSecondEmpty && activeFirstLevel.isNotBlank() ){
        addSecondLevel(activeFirstLevel)
        meanings.last().firstLevel = ""
    }

    meanings.add(FirstMeaning(first))
}
fun PartMeaning.addSecondLevel( meaning : String ){
    meanings.last().meanings.add(SecondMeaning(meaning))
}
fun PartMeaning.addTranslated( translated : Map<String, String> ){
    if( isFirstEmpty ){
        addFirstLevel("")
        addSecondLevel(partLevel)
        partLevel = ""
    }else
        if( isSecondEmpty ){
            addSecondLevel(activeFirstLevel)
            meanings.last().firstLevel = ""
        }

    meanings.last().meanings.last().examples.putAll(translated)
}

//
fun Vocabulary.println(){
    out = "${spell}"
    out = "音節 : ${syllable}  発音記号 : ${phonetic}"
    out = "活用 : ${conjugations}"
    meanings.forEachIndexed { index, partMeaning ->
        out = "  ${index+1} : ${partMeaning.part.code} : ${partMeaning.partLevel}"
        partMeaning.println()
    }
}
fun PartMeaning.println(){
    meanings.forEachIndexed { index, firstMeaning ->
        out = "    ${index+1} : ${firstMeaning.firstLevel}"
        firstMeaning.println()
    }
}
fun FirstMeaning.println(){
    meanings.forEachIndexed { index, secondMeaning ->
        out = "      ${'a'+index} : ${secondMeaning.secondLevel}"
        secondMeaning.println()
    }
}
fun SecondMeaning.println(){
    examples.forEach {
        out = "        ${it.key} // ${it.value}"
    }
}