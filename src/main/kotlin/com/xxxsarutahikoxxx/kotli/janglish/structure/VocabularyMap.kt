package com.xxxsarutahikoxxx.kotli.janglish.structure

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
            AAMap[key]?.put(voc.spell, voc /* voc + still TODO*/)
        }else{
            AAMap[key]?.put(voc.spell, voc)
        }!!
    }
}
