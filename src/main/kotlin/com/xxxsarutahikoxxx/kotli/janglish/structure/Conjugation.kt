package com.xxxsarutahikoxxx.kotli.janglish.structure


/** 活用 */
enum class Conjugation(val code : String) {
    /** 原形 */
    Stem("原形"),

    // 動詞の活用
    /** 三人称単数現在 */
    ThirdPersonSingular("三人称単数現在"),
    /** 過去形 */
    Past("過去形"),
    /** 現在分詞 */
    PresentParticiple("現在分詞"),
    /** 過去分詞 */
    PastParticiple("過去分詞"),

    // 名詞の活用
    /** 複数形 */
    Plural("複数形")
    ;

    companion object {
        fun of(code : String) : Conjugation {
            return values().first { it.code == code }
        }
    }
}
