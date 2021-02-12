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
    fun contains(str : String) : Condition = { str in it.spell }
    fun match(str : String) : Condition = { str == it.spell }
    fun prefix(str : String) : Condition = { it.spell.startsWith(str) }
    fun suffix(str : String) : Condition = { it.spell.endsWith(str) }

    fun pContains(str : String) : Condition = { str in it.phonetic.firstOrNull() ?:"" }
    fun pMatch(str : String) : Condition = { str == it.phonetic.firstOrNull() ?:"" }
    fun pPrefix(str : String) : Condition = { it.phonetic.firstOrNull()?.startsWith(str) ?: false }
    fun pSuffix(str : String) : Condition = { it.phonetic.firstOrNull()?.endsWith(str) ?: false }



    companion object {
        private val mixer = VocabularyMixer()
        internal fun condition(func : VocabularyMixer.()->(Condition)) : Condition = mixer.func()
    }
}
fun vMixer( func : VocabularyMixer.()->(Condition) ) : Condition {
    return VocabularyMixer.condition(func)
}
