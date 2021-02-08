package com.xxxsarutahikoxxx.kotli.janglish.structure


typealias Condition = (Vocabulary)->(Boolean)



class VocabularyMixer private constructor(){

    /** and */
    infix fun Condition.and(nCon : Condition) : Condition {
        return { it : Vocabulary -> this@and.invoke(it) && nCon.invoke(it)  }
    }
    /** or */
    infix fun Condition.or(nCon : Condition) : Condition {
        return { it : Vocabulary -> this@or.invoke(it) || nCon.invoke(it)  }
    }
    /** not / (!) */
    operator fun Condition.not() : Condition {
        return { it : Vocabulary -> ! this@not.invoke(it) }
    }


    // 組み込み条件オブジェクト
    fun contains(str : String) : Condition = { str == it.spell }
    fun match(str : String) : Condition = { str == it.spell }
    fun prefix(str : String) : Condition = { it.spell.startsWith(str) }
    fun suffix(str : String) : Condition = { it.spell.endsWith(str) }


    companion object {
        private val mixer = VocabularyMixer()
        internal fun condition(func : VocabularyMixer.()->(Condition)) : Condition = mixer.func()
    }
}
fun vMixer( func : VocabularyMixer.()->(Condition) ) : Condition {
    return VocabularyMixer.condition(func)
}
