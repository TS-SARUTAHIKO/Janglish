package com.xxxsarutahikoxxx.kotli.janglish

import com.xxxsarutahikoxxx.kotli.janglish.classifier.Eijiro
import com.xxxsarutahikoxxx.kotli.janglish.parser.Weblio
import com.xxxsarutahikoxxx.kotli.janglish.structure.Vocabulary
import com.xxxsarutahikoxxx.kotli.janglish.structure.println
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStreamReader

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
fun String.surrounding(prefix : String, suffix : String) : Boolean {
    return startsWith(prefix) && endsWith(suffix)
}



@Serializable
internal data class Properties(
   private var _ListIndex : Pair<String, Int>? = null
){
    var ListIndex : Pair<String, Int>?
        get() = _ListIndex
        set(value) {
            _ListIndex = value
            save()
        }


    internal fun save(){
        File(address).apply {
            delete()
            writeText(Json.encodeToString(prop))
        }
    }

    companion object {
        internal val address = "src/main/resources/properties.prop"

        internal val prop by lazy {
            val input = InputStreamReader(File(address).inputStream()).readText()

            try{
                val ret = Json.decodeFromString<Properties>(input)
                ret
            }catch (e : Exception){
                Properties()
            }
        }
    }
}
internal val prop get() = Properties.prop

fun List<String>.append(str : String) : String {
    return listOf(*this.toTypedArray(), str).filter { it.isNotBlank() }.joinToString("\n")
}

fun main(args: Array<String>) {

    val spell = when( 1 ) {
        1 -> "tiny"

        else -> null
    }

    if( spell == null ){
        Eijiro.List_Lv3/*.shuffled()*/.subList(prop.ListIndex?.second ?: 0, 100).forEachIndexed { index, spell ->
            out = spell
            prop.ListIndex = spell to index

            Weblio.parse(spell).forEach { it.println() }
            Thread.sleep(2000)
        }
        prop.ListIndex = null
    }else{
        Weblio
            .parse(spell!!)
            .forEachIndexed { index : Int, it ->
                out = "単語 : ${index+1}"

                val string = Json.encodeToString(it)
                Json.decodeFromString<Vocabulary>(string).println()
            }
    }

}