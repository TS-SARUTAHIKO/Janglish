package com.xxxsarutahikoxxx.kotli.janglish.structure

import com.xxxsarutahikoxxx.kotli.janglish.out
import kotlinx.serialization.Serializable

@Serializable
data class Vocabulary(
    /** スペル */
    var spell : String,
    /** コア */
    var summary : MutableMap<PartOfSpeech, String>,
    /** 活用形 */
    var conjugations : MutableMap<Conjugation, MutableList<String>>,
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
    val majorParts : List<PartOfSpeech> get() = parts.map { it.parent }.distinct().filter { ! it.isSpecial }

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
        return meanings.filter { it.part in part }.map { it.firsts }.flatten()
    }
}
@Serializable
data class PartMeaning(
    var part : PartOfSpeech,
    var partLevel : String = "",
    var firsts : MutableList<FirstMeaning> = mutableListOf()
)
@Serializable
data class FirstMeaning(
    var firstLevel : String = "",
    var seconds : MutableList<SecondMeaning> = mutableListOf()
)
@Serializable
data class SecondMeaning(
    var secondLevel : String = "",
    var examples : MutableMap<String /* English */, String /* Japanese */> = mutableMapOf()
)

// Vocabulary
val Vocabulary.allPartLevel get() = meanings.toList()
val Vocabulary.allFirstLevel get() = meanings.map { it.firsts }.flatten()
val Vocabulary.allSecondLevel get() = allFirstLevel.map { it.seconds }.flatten()

// PartMeaning
val PartMeaning.isFirstEmpty get() = firsts.isEmpty()
val PartMeaning.isSecondEmpty get() = firsts.last().seconds.isEmpty()

val PartMeaning.activeFirstLevel : String get() = firsts.last().firstLevel
val PartMeaning.activeSecondLevel : String get() = firsts.last().seconds.last().secondLevel

//
fun Vocabulary.println(){
    out = "${spell}"
    out = "音節 : ${syllable}  発音記号 : ${phonetic}"
    out = "活用 : ${conjugations}"
    out = "概要 : ${summary}"
    meanings.forEachIndexed { index, partMeaning ->
        out = "  ${index+1} : ${partMeaning.part.code} : ${partMeaning.partLevel}"
        partMeaning.println()
    }
}
fun PartMeaning.println(){
    firsts.forEachIndexed { index, firstMeaning ->
        out = "    ${index+1} : ${firstMeaning.firstLevel}"
        firstMeaning.println()
    }
}
fun FirstMeaning.println(){
    seconds.forEachIndexed { index, secondMeaning ->
        out = "      ${'a'+index} : ${secondMeaning.secondLevel}"
        secondMeaning.println()
    }
}
fun SecondMeaning.println(){
    examples.forEach {
        out = "        ${it.key} // ${it.value}"
    }
}