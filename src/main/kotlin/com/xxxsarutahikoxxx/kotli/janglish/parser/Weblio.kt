package com.xxxsarutahikoxxx.kotli.janglish.parser

import com.xxxsarutahikoxxx.kotli.janglish.out
import com.xxxsarutahikoxxx.kotli.janglish.structure.Conjugation
import com.xxxsarutahikoxxx.kotli.janglish.structure.PartOfSpeech
import com.xxxsarutahikoxxx.kotli.janglish.structure.Vocabulary
import com.xxxsarutahikoxxx.kotli.janglish.structure.protoVocabulary
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


object Weblio {

    fun parse(spell: String) : Vocabulary {
        val voc = protoVocabulary(spell)

        val document: Document = Jsoup.connect("https://ejje.weblio.jp/content/$spell").get()

        // voc.level = document.getElementsByClass("learning-level-content")?.firstOrNull()?.text()?.run { Integer.parseInt(this) }

        val conjugations = mutableMapOf<Conjugation, String>()
        document.getElementsByClass("conjugateRowR")?.forEach {
            it.run {
                val map = this.getElementsByTag("span").map { it.text().run { this.substring(1, this.length-1) } }.zip( this.getElementsByTag("a").map { it.text() } )

                map.forEach { conjugations[Conjugation.of(it.first)] = it.second }
            }
        }
        voc.conjugations = conjugations.toMutableMap()

        // 研究社
        document.getElementsByClass("mainBlock hlt_KENEJ").firstOrNull()?.apply {
            voc.syllable = getElementsByClass("KejjeOs").firstOrNull()?.text()
            voc.phonetic = getElementsByClass("KejjeHt").firstOrNull()?.text()?.split(Regex("[ ｜/,]"))?.first() { it.isNotEmpty() }

            getElementsByClass("level0")?.firstOrNull()?.run {
                listOf<Element>( this, *this.siblingElements().toArray(arrayOf<Element>()) ).filter {
                    it.className() in listOf("level0", "KejjeYr")
                }
            }?.forEach {
                if( it.getElementsByTag("audio").isEmpty() ){
                    val part = it.getElementsByClass("KnenjSub")?.firstOrNull()?.text()
                    val category = it.getElementsByClass("lvlNH")?.firstOrNull()?.text()
                    val alphabet = it.getElementsByClass("lvlAH")?.firstOrNull()?.text()
                    val meaning = it.getElementsByClass("lvlB")?.firstOrNull()?.text()
                    val english = it.getElementsByClass("KejjeYrEn")?.firstOrNull()?.text()
                    val japanese = it.getElementsByClass("KejjeYrJp")?.firstOrNull()?.text()


                    if( part?.isNotEmpty() == true ) voc.addNewPart( PartOfSpeech.of(part) )

                    if( category?.isNotEmpty() == true ) voc.addNewCategory()
                    if( alphabet?.isNotEmpty() == true || meaning?.isNotEmpty() == true ) voc.addNewMeaning(meaning!!)

                    if( english?.isNotEmpty() == true && japanese?.isNotEmpty() == true ) voc.addNewSentence(english, japanese)

                    if( part == null && category == null && alphabet == null && english == null && japanese == null ){
                        voc.addNewMeaning( it.text() )
                    }
                }
            }
        }

        return voc.toVoc
    }

}

