package com.xxxsarutahikoxxx.kotlin.janglish.structure

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


@Serializable
abstract class VocLibrary {
    abstract val VocabularyMap : MutableMap<String, MutableList<Vocabulary>>

    /** 単語の分類キーを判別する（単語の先頭2文字をキーとして扱う） */
    val String.key : String get() = (this + this).substring(0, 2).toLowerCase()
    /** 単語の分類キーを判別する（単語の先頭2文字をキーとして扱う） */
    val Vocabulary.key : String get() = spell.key

    /**
     * マップからボキャブラリーを取得する
     * */
    operator fun get(spell : String) : List<Vocabulary> {
        return VocabularyMap[spell.key]?.filter { it.spell == spell }?.toList() ?: listOf()
    }
    /**
     * 条件を指定してボキャブラリーを取得する
     * */
    fun get(condition : Condition) : List<Vocabulary> {
        return VocabularyMap.values.map { it.filter(condition) }.flatten()
    }

    /**
     * マップに追加する
     * */
    fun add(voc : Vocabulary){
        VocabularyMap.putIfAbsent(voc.key, mutableListOf())
        VocabularyMap[voc.key]!!.add(voc)
    }
    /**
     * マップに追加する
     * */
    fun add(map : VocLibrary){
        map.VocabularyMap.map {
            VocabularyMap[it.key]?.addAll( it.value )
        }
    }


    companion object {
        private val library : VocLibrary = VocMapImpl()
        private val format = Json {
            serializersModule = SerializersModule {
                polymorphic(VocLibrary::class) {
                    subclass(VocMapImpl::class)
                }
            }
        }

        val libraryCode : String get(){
            return format.encodeToString(VocLibrary.serializer(), library)
        }
        fun loadLibraryCode(code : String){
            format.decodeFromString<VocLibrary>(code).apply {
                library.add(this)
            }
        }

        fun add(voc : Vocabulary) = library.add(voc)
        fun of(spell : String) : List<Vocabulary> = library[spell]
        fun get( condition : Condition) : List<Vocabulary> = library.get(condition)
    }
}

/**
 * ミキサーを用いてボキャブラリーを取得する
 * */
fun VocLibrary( func : VocabularyMixer.()->(Condition) ) : List<Vocabulary> {
    return VocLibrary.get(VocabularyMixer.condition(func))
}


@Serializable
data class VocMapImpl(
    override val VocabularyMap: MutableMap<String, MutableList<Vocabulary>> = {
        ('a'..'z').map {
            val k1 = it
            ('a'..'z').map { "" + k1 + it }
        }.flatten().associateWith {
            mutableListOf<Vocabulary>()
        }.toMutableMap()
    }.invoke()
) : VocLibrary() {

}
