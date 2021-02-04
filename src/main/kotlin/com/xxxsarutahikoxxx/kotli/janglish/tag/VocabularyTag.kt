package com.xxxsarutahikoxxx.kotli.janglish.tag

import com.xxxsarutahikoxxx.kotli.janglish.directory.TagNode
import com.xxxsarutahikoxxx.kotli.janglish.directory.TagNodeLibrary
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


@Serializable
abstract class VocabularyTag {
    abstract val code : String
    abstract var parentCode : String?
    abstract var name : String

    val parentTag : VocabularyTag? get() = parentCode?.run { TagLibrary.of(this) }
    val childTags : List<VocabularyTag> get()= TagLibrary.childOf(code)


    companion object {
        fun make(code : String, parent : String? = null, name : String) : Pair<VocabularyTag, TagNode> {
            return TagLibrary.instance(code, parent, name) to TagNodeLibrary.instance(code)
        }
    }
}
@Serializable
data class VocabularyTagImpl(override val code : String, override var parentCode : String?, override var name : String) : VocabularyTag()

@Serializable
data class TagLibrary(
    val tags : MutableList<VocabularyTag> = mutableListOf()
){
    companion object {
        private var library : TagLibrary = TagLibrary()

        val libraryCode : String get(){
            // シリアライザーのバグ回避のための処理
            val module = SerializersModule {
                polymorphic(VocabularyTag::class) {
                    subclass(VocabularyTagImpl::class)
                }
            }
            val format = Json { serializersModule = module }

            return format.encodeToString(TagLibrary.serializer(), library)
        }
        fun loadLibraryCode(code : String){
            // シリアライザーのバグ回避のための処理
            val module = SerializersModule {
                polymorphic(VocabularyTag::class) {
                    subclass(VocabularyTagImpl::class)
                }
            }
            val format = Json { serializersModule = module }

            format.decodeFromString<TagLibrary>(code).apply {
                library.tags.addAll(this.tags)
            }
        }


        /**
         * [code] を元にタグを検索する
         * */
        fun of(code : String) : VocabularyTag? {
            return library.tags.firstOrNull { it.code == code }
        }
        /**
         * [code] [parent] [name] を元にタグを検索 or 作成する
         * */
        fun instance(code : String, parent : String? = null, name : String = code) : VocabularyTag {
            return of(code) ?: VocabularyTagImpl(code, parent, name).apply { library.tags.add(this) }
        }
        /**
         * [tagCode] 出死体されたタグの子タグのリストを取得する
         *  */
        fun childOf(tagCode : String) : List<VocabularyTag> {
            return library.tags.filter { it.code == tagCode }
        }
    }
}