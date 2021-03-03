package com.xxxsarutahikoxxx.kotlin.janglish.structure

import com.xxxsarutahikoxxx.kotlin.Utilitys.getResourceAsStream
import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.lang.RuntimeException


@Serializable
data class PhoneticSymbol(
    val symbols : List<String>,
    val howToPronounce : String,
    val tips : String,
    val examples : List<String>
)

@Serializable
data class PhoneticLibrary(
    private val phonetics : MutableList<PhoneticSymbol> = mutableListOf()
){
    @Transient
    private var phoneticMap : Map<Int, Map<String, PhoneticSymbol>> = mapOf()
    private fun loadPhoneticMap(){
        val max = library.phonetics.maxOf { it.symbols.maxOf { it.length } }

        phoneticMap = (0..max)
                .associateWith { length ->
                    library.phonetics
                            .map { symbol ->
                                symbol.symbols.filter { it.length == length }.map { it to symbol }
                            }
                            .flatten()
                            .toMap()
                }
    }


    companion object {
        private var library: PhoneticLibrary = PhoneticLibrary()
        private val decorations = listOf('(', ')', '-', 'ˈ', 'ˌ', '‐', '`')
        private val phoneticMap get() = library.phoneticMap

        // シリアライザーのバグ回避のための処理
        private val format = Json {
            serializersModule = SerializersModule {
//                polymorphic(VocabularyTag::class) {
//                    subclass(VocabularyTagImpl::class)
//                }
            }
        }

        val libraryCode: String
            get() {
                return format.encodeToString(PhoneticLibrary.serializer(), library)
            }

        fun loadLibraryCode(code: String) {
            format.decodeFromString<PhoneticLibrary>(code).apply {
                library.phonetics.addAll(this.phonetics)
                library.loadPhoneticMap()
            }
        }
        fun of(code : String) : PhoneticSymbol? {
            return library.phonetics.firstOrNull { code in it.symbols }
        }

        fun loadDefault(){
            val texts = getResourceAsStream("PhoneticSymbol.json").use {
                it.bufferedReader().readText()
            }
            val list = Json {  }.decodeFromString<List<PhoneticSymbol>>(texts)

            library.phonetics.addAll(list)
            library.loadPhoneticMap()
        }

        fun parse(phoneticCode : String) : List<String> {
            val map = phoneticMap

            val ret = mutableListOf<String>()

            var code = phoneticCode
            while( code.isNotEmpty() ){
                when {
                    code[0] in decorations -> {
                        ret.add(code[0].toString())
                        code = code.substring(1, code.length)
                    }
                    map[4]?.keys?.any { code.startsWith(it) } == true -> {
                        ret.add(code.substring(0, 4))
                        code = code.substring(4, code.length)
                    }
                    map[3]?.keys?.any { code.startsWith(it) } == true -> {
                        ret.add(code.substring(0, 3))
                        code = code.substring(3, code.length)
                    }
                    map[2]?.keys?.any { code.startsWith(it) } == true -> {
                        ret.add(code.substring(0, 2))
                        code = code.substring(2, code.length)
                    }
                    map[1]?.keys?.any { code.startsWith(it) } == true -> {
                        ret.add(code.substring(0, 1))
                        code = code.substring(1, code.length)
                    }
                    else -> {
                        out = "Error : $phoneticCode : $code"
                        throw RuntimeException("")
                    }
                }
            }

            return ret
        }
    }
}