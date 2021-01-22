package com.xxxsarutahikoxxx.kotli.janglish

import com.xxxsarutahikoxxx.kotli.janglish.classifier.Eijiro
import com.xxxsarutahikoxxx.kotli.janglish.classifier.Oxford3000
import com.xxxsarutahikoxxx.kotli.janglish.parser.Oxford
import com.xxxsarutahikoxxx.kotli.janglish.parser.Weblio
import com.xxxsarutahikoxxx.kotli.janglish.structure.PartOfSpeech

var outstream : (Any?)->(Unit) = { println("$it") }
var out : Any?
    get() = null
    set(value) { outstream(value) }

var errorstream : (Any?)->(Unit) = { println("$it") }
var error : Any?
    get() = null
    set(value) { errorstream(value) }


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
/**
 * [Decorations]で区切られた部分を除去する
 *
 * 区切り文字を統一する
 * */
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

    ret = ret.replace("[,.;，]".toRegex(), " ").replace("  ", " ")
    ret = ret.split(" ").filter { it.isNotEmpty() }.joinToString(", ")

    return ret
}





fun main(args: Array<String>) {
    val spell = "respect"

    Weblio.parse(spell)
//        .run {
//            val pair = Oxford.parse(spell)
//            this.phonetic = pair.first
//            this.resource = pair.second
//
//            this.toVoc
//        }
        .apply {
            meanings.values.forEach { it.forEach { it.entries.forEach {
                out = it.key.noDecorated
            } } }
        }

}