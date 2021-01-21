package com.xxxsarutahikoxxx.kotli.janglish.structure

import kotlinx.serialization.Serializable

@Serializable
data class Vocabulary(
    /** スペル */
    val spell : String,
    /** コア */
    val summary : Map<PartOfSpeech, String>,
    /** 活用形 */
    val conjugations : Map<Conjugation, String>,
    /** 音節 */
    val syllable : String,
    /** 発音記号 */
    val phonetic : String,
    /** 発音リソース */
    var resource : String? = null,

    /** タグ */
    val tags : List<String>,

    /** 句動詞 */
    val phrasal : Map<String, String>,
    /** 類義語 */
    val synonyms : Map<String, Map<String, String>>,
    /** 対義語 */
    val antonyms : Map<String, Map<String, String>>,
    /** 関連語 */
    val related : Map<String, String>,

    /** 意味 */
    val meanings : Map<PartOfSpeech, List<Map< String, Map<String, String> >>>,

    /** 評価値 */
    var evaluation  : Float
){
    /** 品詞のリスト */
    val parts : List<PartOfSpeech> get() = meanings.keys.toList()
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


    fun meaningsOfPart(part : PartOfSpeech) : List<String> {
        return meanings.filterKeys { it in part }
            .map { it.value }.flatten()
                .map { it.keys }.flatten()
    }
}

internal class protoVocabulary(
    val spell : String
){
    /** コア */
    var summary : Map<PartOfSpeech, String> = mapOf()
    /** 活用形 */
    var conjugations : MutableMap<Conjugation, String> = mutableMapOf()
    /** 音節 */
    var syllable : String? = null
    /** 発音記号 */
    var phonetic : String? = null
    /** 発音リソース */
    var resource : String? = null

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

    /** 意味 */
    var meanings : MutableMap<PartOfSpeech, MutableList<MutableMap< String, MutableMap<String, String> >>> = mutableMapOf()


//    動詞 他動詞
//    1 a 〈水分・熱・光などを〉吸収する，吸い上げる[込む].
//        A sponge absorbs water. 海綿は水を吸収する.
//      b 〈音・衝撃などを〉消す，やわらげる，緩和する.
//        absorb shocks 衝撃を吸収する.
//    2 a 〈思想・学問を〉取り入れる，吸収する，同化する.
//      b 〈会社・町村などを〉吸収合併する.

    val currentPart get() = meanings.values.last()
    val currentCategory get() = currentPart.last()
    val currentMeaning get() = currentCategory.keys.last()
    val currentExamples get() = currentCategory.values.last()

    fun addNewPart(part : PartOfSpeech){
//        out = "nP:${part.code}"

        meanings[part] = mutableListOf()
    }
    fun addNewCategory(){
//        out = "nC:"

        currentPart.add(mutableMapOf())
    }
    fun addNewMeaning(meaning : String){
//        out = "nM:$meaning"

        if( currentPart.isEmpty() ) addNewCategory()

        currentCategory[meaning] = mutableMapOf()
    }
    fun addNewSentence(sentence : String, translated : String){
//        out = "nE:$sentence:$translated"

        currentExamples[sentence] = translated
    }

    val toVoc : Vocabulary get(){
        return Vocabulary(
            spell = spell,
            summary = summary,
            conjugations = conjugations,
            syllable = syllable ?: "",
            phonetic = phonetic ?: "",
            resource = resource,
            tags = tags,
            phrasal = phrasal,
            synonyms = synonyms,
            antonyms = antonyms,
            related = related,
            meanings = meanings,
            evaluation = 3f
        )
    }
}

/**
 * 二つの[Vocabulary]を合成する関数
 *
 * TODO : 現状は適当な合成
 * */
private operator fun Vocabulary.plus(voc : Vocabulary) : Vocabulary {
    return Vocabulary(
        spell = spell,
        summary = summary,
        conjugations = conjugations + voc.conjugations,
        syllable = syllable,
        phonetic = phonetic,
        resource = resource,
        tags = tags + voc.tags,
        phrasal = phrasal + voc.phrasal,
        synonyms = synonyms + voc.synonyms,
        antonyms = antonyms + voc.antonyms,
        related = related + voc.related,
        meanings = meanings,
        evaluation = evaluation
    )
}


object VocabularyMap {
    /**
     * 単語のアルファベット先頭2文字を分類用のカギとして使用したマップ
     * */
    internal val AAMap : MutableMap<String, MutableMap<String, Vocabulary>> = {
        ('a'..'z').map {
            val k1 = it
            ('a'..'z').map { "" + k1 + it }
        }.flatten().associateWith {
            mutableMapOf<String, Vocabulary>()
        }.toMutableMap()
    }.invoke()
   /**
     * マップからボキャブラリーを取得する
     * */
    operator fun get(spell : String) : Vocabulary? {
        val key = (spell + spell).substring(0, 2)
        return AAMap[key]?.get(spell)
    }
    /**
     * マップにボキャブラリーを追加する
     *
     * 既に存在する場合は合成した新たな要素を追加する
     * */
    fun put(voc : Vocabulary) : Vocabulary {
        val key = (voc.spell + voc.spell).substring(0, 2)
        val still = get(voc.spell)

        return if( still != null ){
            AAMap[key]?.put(voc.spell, voc + still)
        }else{
            AAMap[key]?.put(voc.spell, voc)
        }!!
    }

}