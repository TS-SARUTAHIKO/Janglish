package com.xxxsarutahikoxxx.kotli.janglish.tag


class VocabularyTag(var name : String, var parent : String) {
    init {
        tags.add(this)
    }
    fun delete(){
        tags.remove(this)
    }

    companion object {
        private const val separator = "/"

        private val tags = mutableListOf<VocabularyTag>()

        /** 名前が一致するタグを検索する */
        fun ofName(name : String) : VocabularyTag? = tags.firstOrNull { it.name == name }
    }
}