package com.xxxsarutahikoxxx.kotli.janglish.directory

import com.xxxsarutahikoxxx.kotli.janglish.tag.TagLibrary
import com.xxxsarutahikoxxx.kotli.janglish.tag.VocabularyTag
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
    abstract val vocabularys : MutableSet<String>

    abstract val tag : VocabularyTag?
    abstract val childNodes : List<TagNode>
}
@Serializable
data class TagNodeImpl(
    override val tagCode : String,
    override val vocabularys : MutableSet<String> = mutableSetOf()
) : TagNode() {
    override val tag : VocabularyTag? by lazy { TagLibrary.of(tagCode) }
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
    }
}