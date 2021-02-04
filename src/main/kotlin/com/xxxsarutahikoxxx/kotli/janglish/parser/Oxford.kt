package com.xxxsarutahikoxxx.kotli.janglish.parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document


object Oxford {

    internal fun parse(spell : String) : Pair<String, String> {
        val document: Document = Jsoup.connect("https://www.oxfordlearnersdictionaries.com/definition/american_english/$spell").get()

        document.getElementsByClass("pron-g").parents().first().apply {
            val phonetic = document.getElementsByClass("phon")?.textNodes()?.firstOrNull()?.text()!!
            val resource = document.getElementsByClass("sound audio_play_button pron-usonly icon-audio")?.firstOrNull()?.attr("data-src-mp3")!!

            return phonetic to resource
        }
    }
}
