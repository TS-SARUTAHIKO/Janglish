package com.xxxsarutahikoxxx.kotli.janglish.directory

import com.xxxsarutahikoxxx.kotlin.janglish.tag.TagLibrary
import com.xxxsarutahikoxxx.kotlin.janglish.tag.VocabularyTag
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


interface VNode

@Serializable
abstract class TagNode : VNode {
    abstract val tagCode : String
    abstract val vocabularies : MutableSet<String>

    abstract val childNodes : List<TagNode>

    val tag : VocabularyTag? get() = TagLibrary.of(tagCode)
}
@Serializable
data class TagNodeImpl(
    override val tagCode : String,
    override val vocabularies : MutableSet<String> = mutableSetOf()
) : TagNode() {
    override val childNodes: List<TagNode> get() = tag?.childTags?.mapNotNull { TagNodeLibrary.of(it.code) } ?: listOf()
}

@Serializable
data class TagNodeLibrary(
    val nodes : MutableList<TagNode> = mutableListOf()
) {
    companion object {
        private val library : TagNodeLibrary = TagNodeLibrary()
        val libraryCode : String get(){
            // シリアライザーのバグ回避のための処理
            val module = SerializersModule {
                polymorphic(TagNode::class) {
                    subclass(TagNodeImpl::class)
                }
            }
            val format = Json { serializersModule = module }

            return format.encodeToString(TagNodeLibrary.serializer(), library)
        }
        fun loadLibraryCode(code : String){
            val module = SerializersModule {
                polymorphic(TagNode::class) {
                    subclass(TagNodeImpl::class)
                }
            }
            val format = Json { serializersModule = module }

            format.decodeFromString<TagNodeLibrary>(code).apply {
                library.nodes.addAll(this.nodes)
            }
        }

        fun of(tagCode : String) : TagNode? {
            return library.nodes.firstOrNull { it.tagCode == tagCode }
        }
        fun instance(tagCode : String) : TagNode {
            return of( tagCode ) ?: TagNodeImpl(tagCode = tagCode).apply { library.nodes.add(this) }
        }
        /**
         * [tagCode] で指定されたタグノードの子タグノードのリストを取得する
         *  */
        fun childOf(tagCode : String? = null) : List<TagNode> {
            return library.nodes.filter { it.tag?.parentTag?.code == tagCode }
        }
        /**
         * トップレベルタグノードを取得する（親タグコードを持たない or 親タグが存在しない タグ）
         * */
        fun topLevels() = childOf(null)

        /**
         * タグを条件付けて検索する
         * */
        fun filtered(condition : (TagNode)->(Boolean) ) : List<TagNode> {
            return library.nodes.filter(condition)
        }
    }
}

/** [TagNodeLibrary.filtered] へのショートカット */
fun TagNodeLibrary( condition : (TagNode)->(Boolean) ) : List<TagNode> = TagNodeLibrary.filtered(condition)
