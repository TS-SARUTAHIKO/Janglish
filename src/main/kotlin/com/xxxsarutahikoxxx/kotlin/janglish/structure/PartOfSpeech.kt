package com.xxxsarutahikoxxx.kotlin.janglish.structure

import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import java.lang.RuntimeException

/** 品詞 */
enum class PartOfSpeech(val code : String, val initial : String, val subcode : String = code) {
    /** 名詞 */
    Noun("名詞", "名"),
    /** 動詞 */
    Verb("動詞", "動"),
    /** 助動詞 */
    ModalVerb("助動詞", "助"),
    /** 形容詞 */
    Adjective("形容詞", "形"),
    /** 冠詞 */
    Article("冠詞", "冠"),
    /** 副詞 */
    Adverb("副詞", "副"),
    /** 前置詞 */
    Preposition("前置詞", "前"),
    /** 代名詞 */
    Pronoun("代名詞", "代"),
    /** 接続詞 */
    Conjunction("接続詞", "接"),
    /** 間投詞 */
    Interjection("間投詞", "間"),

    /** 自動詞 */
    IntransitiveVerb("自動詞", "自", "動詞 [自]"),
    /** 他動詞 */
    TransitiveVerb("他動詞", "他", "動詞 [他]"),
    /** 句動詞 */
    PhrasalVerb("句動詞", "句", "動詞 [句]"),

    /** 可算名詞 */
    CountableNoun("可算名詞", "可算", "名詞 [可算]"),
    /** 不可算名詞 */
    UncountableNoun("不可算名詞", "不可算", "名詞 [不可算]"),

    /** 限定用法 */
    AttributiveAdjective("限定用法形容詞", "限定", "形容詞 [限定]"),
    /** 叙述用法 */
    PredicativeAdjective("叙述用法形容詞", "叙述", "形容詞 [叙述]"),

    /** その他 */
    Overview("単語", "単語"),
    Others("その他", "その他"),

    /**  */
    Modifier("形態素", "形態素"),
    Prefix("接頭辞", "接頭辞"),
    Suffix("接尾辞", "接尾辞")
    ;

    val isNoun : Boolean get() = this in Noun
    val isVerb : Boolean get() = this in Verb
    val isAdjective : Boolean get() = this in Adjective
    val isAdverb : Boolean get() = this in Adverb
    val isModifier : Boolean get() = this in Modifier

    val isSpecial : Boolean get() = this in listOf(Overview, Others)

    val parent : PartOfSpeech
        get(){
        return when (this) {
            in Verb -> Verb
            in Noun -> Noun
            in Adjective -> Adjective
            in Modifier -> Modifier

            else -> this
        }
    }
    val children : List<PartOfSpeech> get(){
        return PartOfSpeech.values().filter { it in this }
    }
    
    operator fun contains( part : PartOfSpeech) =
        part in when( this ){
            Verb -> listOf(this, IntransitiveVerb, TransitiveVerb, PhrasalVerb)
            Noun -> listOf(this, CountableNoun, UncountableNoun)
            Adjective -> listOf(this, AttributiveAdjective, PredicativeAdjective)
            Modifier -> listOf(this, Prefix, Suffix)

            else -> listOf(this)
        }


    companion object {
        fun of(code : String) : PartOfSpeech {
            return when( code ){
                "助動詞" -> ModalVerb
                "冠詞" -> Article
                "副詞" -> Adverb
                "前置詞" -> Preposition
                "代名詞" -> Pronoun
                "接続詞" -> Conjunction
                "前置詞" -> Preposition
                "間投詞" -> Interjection

                "名詞" -> Noun
                "可算名詞", "名詞可算名詞" -> CountableNoun
                "不可算名詞", "名詞不可算名詞" -> UncountableNoun

                "動詞" -> Verb
                "自動詞", "動詞 自動詞" -> IntransitiveVerb
                "他動詞", "動詞 他動詞" -> TransitiveVerb
                "句動詞", "動詞 句動詞" -> PhrasalVerb

                "形容詞" -> Adjective
                "限定用法", "形容詞限定用法の形容詞" -> AttributiveAdjective
                "叙述用法", "形容詞叙述用法の形容詞", "形容詞叙述的用法の形容詞" -> PredicativeAdjective

                "【語源】" -> Others

                "接頭辞", "【接頭辞】" -> Prefix
                "接尾辞", "【接尾辞】" -> Suffix

                else -> { out = code ; throw RuntimeException("$code に体操する品詞が見つかりません") }
            }
        }
        val majors : List<PartOfSpeech> get() = values().map { it.parent }.distinct()
    }
}

