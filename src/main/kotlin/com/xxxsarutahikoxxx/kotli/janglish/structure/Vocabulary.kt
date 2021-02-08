package com.xxxsarutahikoxxx.kotli.janglish.structure

import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import com.xxxsarutahikoxxx.kotli.janglish.tag.TagLibrary
import com.xxxsarutahikoxxx.kotli.janglish.tag.VocabularyTag
import kotlinx.serialization.Serializable
import java.util.*

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
    var resource : MutableList<Pronounce>,

    /** タグ */
    var tagCodes : MutableList<String>,

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

    /**  */
    fun hasMajor(part : PartOfSpeech) : Boolean {
        return part in majorParts
    }
    val hasNoun get() = hasMajor(PartOfSpeech.Noun)
    val hasVerb get() = hasMajor(PartOfSpeech.Verb)
    val hasAdjective get() = hasMajor(PartOfSpeech.Adjective)
    val hasAdverb get() = hasMajor(PartOfSpeech.Adverb)

    /** 活用形の存在 */
    fun has(con : Conjugation) : Boolean = conjugations.containsKey(con)
    /** 過去形を持つかどうか */
    val hasPastForm get() = has(Conjugation.Past)
    /** 過去分詞形を持つかどうか */
    val hasPastParticipleForm get() = has(Conjugation.PastParticiple)
    /** 現在分詞形を持つかどうか */
    val hasPresentParticipleForm get() = has(Conjugation.PresentParticiple)
    /** 三人称単数形を持つかどうか */
    val hasThirdPersonSingularForm get() = has(Conjugation.ThirdPersonSingular)
    /** 複数形を持つかどうか */
    val hasPluralForm get() = has(Conjugation.Plural)
    /** 比較級を持つかどうか */
    val hasComparativeForm get() = has(Conjugation.Comparative)
    /** 最上級を持つかどうか */
    val hasSuperlativeForm get() = has(Conjugation.Superlative)

    /** 活用形を取得する */
    fun form(con : Conjugation) = conjugations[con]
    /** 過去形を取得する */
    val formOfPast get() = form(Conjugation.Past)
    /** 過去分詞形を取得する */
    val formOfPastParticiple get() = form(Conjugation.PastParticiple)
    /** 現在分詞形を取得する */
    val formOfPresentParticiple get() = form(Conjugation.PresentParticiple)
    /** 三人称単数形を取得する */
    val formOfThirdPersonSingular get() = form(Conjugation.ThirdPersonSingular)
    /** 複数形を取得する */
    val formOfPlural get() = form(Conjugation.Plural)
    /** 比較級を取得する */
    val formOfComparative get() = form(Conjugation.Comparative)
    /** 最上級を取得する */
    val formOfSuperlative get() = form(Conjugation.Superlative)


    /** タグをインスタンス化して返却する */
    val tagInstances : List<VocabularyTag> get(){
        return tagCodes.mapNotNull { TagLibrary.of(it) }
    }


    /** [part]に属する[FirstMeaning]のリスト */
    fun meaningsOfMajor(part : PartOfSpeech) : List<FirstMeaning> {
        val part = part.parent
        return meanings.filter { it.part in part }.map { it.firsts }.flatten()
    }

    companion object
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

@Serializable
data class Pronounce(
    /** スペル */
    var spell : String,
    /** 発音記号 */
    var phonetic : String,
    /** リソース */
    var url : String,
    /** 分類用タグ */
    var tags : MutableSet<String> = mutableSetOf()
){
    var isAmerican : Boolean
        get() = Locale.US.toString() in tags
        set(value) {
            val key = Locale.US.toString()
            if( value ){ tags.add(key) }else{ tags.remove(key) }
        }
    var isBritish : Boolean
        get() = Locale.UK.toString() in tags
        set(value) {
            val key = Locale.UK.toString()
            if( value ){ tags.add(key) }else{ tags.remove(key) }
        }
    var isOxford : Boolean
        get() = "Oxford" in tags
        set(value) {
            val key = "Oxford"
            if( value ){ tags.add(key) }else{ tags.remove(key) }
        }
    var isWeblio : Boolean
        get() = "Weblio" in tags
        set(value) {
            val key = "Weblio"
            if( value ){ tags.add(key) }else{ tags.remove(key) }
        }
}

// Vocabulary
val Vocabulary.allPartLevel get() = meanings.toList()
val Vocabulary.allFirstLevel get() = meanings.map { it.firsts }.flatten()
val Vocabulary.allSecondLevel get() = allFirstLevel.map { it.seconds }.flatten()


//
fun Vocabulary.println(){
    out = "${spell}"
    out = "音節 : ${syllable}  発音記号 : ${phonetic}"
    out = "活用 : ${conjugations}"
    out = "タグ : ${tagCodes}"
    out = "リソース : ${resource}"
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


//
private val weblioTag = TagLibrary.instance("Weblio", "Classifier")
