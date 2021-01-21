package com.xxxsarutahikoxxx.kotli.janglish

import com.xxxsarutahikoxxx.kotli.janglish.parser.Oxford
import com.xxxsarutahikoxxx.kotli.janglish.parser.Weblio
import com.xxxsarutahikoxxx.kotli.janglish.structure.PartOfSpeech
import com.xxxsarutahikoxxx.kotli.janglish.structure.VocabularyMap

var out : Any?
    get() = null
    set(value) { println(value) }


private val Decorations = listOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
    '〔' to '〕',
    '《' to '》',
    '〈' to '〉',
    '［' to '］'
)
val String.noDecorated : String get(){
    var ret = this

    do{
        val loop = Decorations.firstOrNull { ret.contains(it.first) && ret.contains(it.second) }?.also {
            val open = it.first
            val close = it.second

            val fIndex = ret.indexOf(open)
            var index = fIndex

            while(true){
                val lIndex = ret.indexOf(close, index+1)

                if( ret.substring(fIndex, lIndex+1).run { count { it == open } == count { it == close } } ){
                    ret = ret.removeRange(fIndex, lIndex+1)
                    break
                }else{
                    index = lIndex
                }
            }
        } != null
    }while( loop )

    return ret
}



fun main(args: Array<String>) {
    val spell = "preservation"

    Weblio.parse(spell).run {
        val pair = Oxford.parse(spell)
        this.phonetic = pair.first
        this.resource = pair.second

        this.toVoc
    }.apply {
        meaningsOfPart(PartOfSpeech.Noun).forEach {
            out = it.noDecorated
        }
    }

}