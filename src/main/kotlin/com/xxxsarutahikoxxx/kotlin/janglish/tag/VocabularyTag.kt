package com.xxxsarutahikoxxx.kotlin.janglish.tag

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
    abstract var tips : String

    val parentTag : VocabularyTag? get() = parentCode?.run { TagLibrary.of(this) }
    val childTags : List<VocabularyTag> get()= TagLibrary.childOf(code)

    val tagNode : TagNode? get() = TagNodeLibrary.of(code)


    companion object {
        /**
         * [code], [parent], [name] でタグとタグノードのペアを作成する
         *
         * 既に存在する場合はそれを返す
         * */
        fun make(code : String, parent : String? = null, name : String) : Pair<VocabularyTag, TagNode> {
            return TagLibrary.instance(code, parent, name) to TagNodeLibrary.instance(code)
        }
    }
}
@Serializable
data class VocabularyTagImpl(override val code : String, override var parentCode : String?, override var name : String, override var tips : String) : VocabularyTag()

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
            return of(code) ?: VocabularyTagImpl(code, parent, name, "").apply { library.tags.add(this) }
        }
        /**
         * [tagCode] で指定されたタグの子タグのリストを取得する
         *  */
        fun childOf(tagCode : String? = null) : List<VocabularyTag> {
            return library.tags.filter { it.parentTag?.code == tagCode }
        }
        /**
         * トップレベルタグを取得する（親タグコードを持たない or 親タグが存在しない タグ）
         * */
        fun topLevels() = childOf(null)

        /**
         * タグを条件付けて検索する
         * */
        fun filtered(condition : (VocabularyTag)->(Boolean) ) : List<VocabularyTag> {
            return library.tags.filter(condition)
        }
    }
}

/** [TagLibrary.filtered] へのショートカット */
fun TagLibrary( condition : (VocabularyTag)->(Boolean) ) : List<VocabularyTag> = TagLibrary.filtered(condition)
