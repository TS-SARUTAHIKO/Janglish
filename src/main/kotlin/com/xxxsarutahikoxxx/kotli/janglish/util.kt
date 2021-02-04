package com.xxxsarutahikoxxx.kotli.janglish

import com.xxxsarutahikoxxx.kotli.janglish.classifier.Eijiro
import com.xxxsarutahikoxxx.kotli.janglish.directory.TagNodeLibrary
import com.xxxsarutahikoxxx.kotli.janglish.parser.Weblio
import com.xxxsarutahikoxxx.kotli.janglish.structure.VocLibrary
import com.xxxsarutahikoxxx.kotli.janglish.structure.Vocabulary
import com.xxxsarutahikoxxx.kotli.janglish.structure.println
import com.xxxsarutahikoxxx.kotli.janglish.tag.TagLibrary
import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*
import javax.swing.JFrame


private class Loader {
    companion object {
        fun getResourceAsStream(name : String) : InputStream =
                Loader::class.java.getResourceAsStream("/$name")
    }
}
fun getResourceAsStream(name : String) = Loader.getResourceAsStream(name)


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
    TagLibrary.loadLibraryCode("{\"tags\":[{\"type\":\"com.xxxsarutahikoxxx.kotli.janglish.tag.VocabularyTagImpl\",\"code\":\"human\",\"parentCode\":null,\"name\":\"人間\"},{\"type\":\"com.xxxsarutahikoxxx.kotli.janglish.tag.VocabularyTagImpl\",\"code\":\"human parts\",\"parentCode\":\"human\",\"name\":\"身体パーツ\"}]}\n")
    TagNodeLibrary.loadLibraryCode("{\"nodes\":[{\"type\":\"com.xxxsarutahikoxxx.kotli.janglish.directory.TagNodeImpl\",\"tagCode\":\"human\",\"vocabularys\":[]},{\"type\":\"com.xxxsarutahikoxxx.kotli.janglish.directory.TagNodeImpl\",\"tagCode\":\"human parts\",\"vocabularys\":[\"sample\",\"ggg\",\"aaa\"]}]}\n")

//    VocabularyTag.make("human", null, "人間")
//    VocabularyTag.make("human parts", "human", "身体パーツ").second.apply {
//        vocabularys.addAll(listOf("sample", "ggg", "aaa"))
//    }
//    out = TagLibrary.libraryCode
//    out = TagNodeLibrary.libraryCode
//
//
//
    val spell = when( 1 ) {
        1 -> "assign"
        else -> null
    }
    if( spell == null ){
        Eijiro.List_Lv5/*.shuffled()*/.subList(prop.ListIndex?.second ?: 0, 200).forEachIndexed { index, spell ->
            out = spell
            prop.ListIndex = spell to index

            Weblio.parse(spell).forEach { VocLibrary.add(it) ; it.println() }
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


    getResourceAsStream("weblio5_200.txt").use {
        it.bufferedReader().use {
            VocLibrary.loadLibraryCode(it.readText())
        }
    }


    JFrame().apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }
}