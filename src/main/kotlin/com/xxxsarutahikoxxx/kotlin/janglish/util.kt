package com.xxxsarutahikoxxx.kotlin.janglish

import com.xxxsarutahikoxxx.kotlin.Feature.TreeRoot
import com.xxxsarutahikoxxx.kotlin.Utilitys.getResourceAsStream
import com.xxxsarutahikoxxx.kotlin.janglish.classifier.Eijiro
import com.xxxsarutahikoxxx.kotlin.janglish.parser.Weblio
import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import com.xxxsarutahikoxxx.kotlin.janglish.structure.VocLibrary
import com.xxxsarutahikoxxx.kotlin.janglish.structure.Vocabulary
import com.xxxsarutahikoxxx.kotlin.janglish.structure.println
import com.xxxsarutahikoxxx.kotlin.janglish.tag.TagLibrary
import com.xxxsarutahikoxxx.kotlin.janglish.tag.VocabularyTag
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*


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
    return (this + str).filter { it.isNotBlank() }.joinToString("\n")
}


fun main(args: Array<String>) {


    val spell = when( null ) {
        1 -> "break"
        else -> null
    }
    if( spell == null ){
        Eijiro.List_Lv5/*.shuffled()*/.subList(prop.ListIndex?.second ?: 0, 200).forEachIndexed { index, spell ->
            out = "$spell : $index"
            prop.ListIndex = spell to index

            Weblio.parse(spell).forEach { VocLibrary.add(it) ; it.println() }
            Thread.sleep(2000)
        }
        prop.ListIndex = null
        File("src/main/resources/weblio5_sample.txt").out = VocLibrary.libraryCode
    }else{
        Weblio
            .parse(spell!!)
            .forEachIndexed { index : Int, it ->
                out = "単語 : ${index+1}"

                VocLibrary.add(it)

                val string = Json.encodeToString(it)
                Json.decodeFromString<Vocabulary>(string).println()
            }
        val code = VocLibrary.libraryCode
        VocLibrary { prefix("break") }.forEach {
            it.println()
        }
    }





//    val texts = getResourceAsStream("PhoneticSymbol.json").use {
//        it.bufferedReader().readText()
//    }
//    out = Json {  }.decodeFromString<List<PhoneticSymbol>>(texts)
}

fun loadTags(){
    getResourceAsStream("tags.txt").use {
        it.bufferedReader().use {
            TagLibrary.loadLibraryCode(it.readText())
        }
    }
}
