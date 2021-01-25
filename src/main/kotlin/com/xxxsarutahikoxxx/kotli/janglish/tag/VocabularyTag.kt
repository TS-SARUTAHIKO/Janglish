package com.xxxsarutahikoxxx.kotli.janglish.tag


class VocabularyTag private constructor(var name : String, var parent : String?) {
    fun delete(){
        tags.remove(this)
    }


    companion object {
        private const val separator = "/"

        private val tags = mutableListOf<VocabularyTag>()


        /**
         * [name] [parent] を元にタグを検索 or 作成する
         *
         * [name] が一致するものがある場合は既存のものが用いられる
         * */
        fun of(name : String, parent : String? = null) : VocabularyTag {
            return tags.firstOrNull { it.name == name } ?: VocabularyTag(name, parent).apply { tags.add(this) }
        }
    }
}