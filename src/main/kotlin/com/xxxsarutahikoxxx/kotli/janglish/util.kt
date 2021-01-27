package com.xxxsarutahikoxxx.kotli.janglish

import com.xxxsarutahikoxxx.kotli.janglish.parser.Weblio
import com.xxxsarutahikoxxx.kotli.janglish.parser.Wiktionary
import com.xxxsarutahikoxxx.kotli.janglish.structure.Vocabulary
import com.xxxsarutahikoxxx.kotli.janglish.structure.println
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

var outstream : (Any?)->(Unit) = { println("$it") }
var out : Any?
    get() = null
    set(value) { outstream(value) }

var errorstream : (Any?)->(Unit) = { println("$it") }
var error : Any?
    get() = null
    set(value) { errorstream(value) }

val String.removeSpaceSurrounding : String get(){
    return this.replace("^ +| +$".toRegex(), "")
}

@Serializable
data class Human(val name : String, val age : Int)

fun main(args: Array<String>) {
    val spell = "real"

    Weblio
        .parse(spell)
        .forEachIndexed { index : Int, it ->
            out = "単語 : ${index+1}"
//            it.println()

            val string = Json.encodeToString(it)
            Json.decodeFromString<Vocabulary>(string).println()
        }

//    Wiktionary.createDerivedAndRelated("respect").forEach {
//        out = it
//    }

}