package com.xxxsarutahikoxxx.kotli.janglish

import com.xxxsarutahikoxxx.kotli.janglish.parser.Weblio
import com.xxxsarutahikoxxx.kotli.janglish.structure.println
import com.xxxsarutahikoxxx.kotli.janglish.suffix.Suffix
import java.lang.Exception

var outstream : (Any?)->(Unit) = { println("$it") }
var out : Any?
    get() = null
    set(value) { outstream(value) }

var errorstream : (Any?)->(Unit) = { println("$it") }
var error : Any?
    get() = null
    set(value) { errorstream(value) }


fun main(args: Array<String>) {
    val spell = "inspect"

    Weblio
        .parse(spell)
        .forEach {
            it.println()
        }

}