package com.xxxsarutahikoxxx.kotlin.janglish.parser

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.jsoup.Jsoup

object GoogleScript {
    private val codes = listOf("text", "text2", "text3", "text4")

    fun toJapanese(vararg en : String ) : List<String> {
        if( en.isEmpty() ) return listOf()

        val map = codes.zip( en.toList() )

        var url = "https://script.google.com/macros/s/AKfycbwPN_ki7g7hqQsZlEOjW8YAx4FoKCxHZyriIT15LbcPH4sbBEg06gm2/exec"
        url += "?source=en&target=ja&length=${map.size}${ map.joinToString(""){ "&${it.first}=${it.second}" } }"

        val content : String = Jsoup.connect(url).get().body().text()
        val ret = Json.decodeFromString<JsonObject>(content)

        val length = ret["length"].run { Integer.parseInt("$this") }
        return List(length){ ret[ codes[it] ].toString() }
    }
    fun toEnglish(vararg ja : String ) : List<String> {
        if( ja.isEmpty() ) return listOf()

        val map = codes.zip( ja.toList() )

        var url = "https://script.google.com/macros/s/AKfycbwPN_ki7g7hqQsZlEOjW8YAx4FoKCxHZyriIT15LbcPH4sbBEg06gm2/exec"
        url += "?source=ja&target=en&length=${map.size}${ map.joinToString(""){ "&${it.first}=${it.second}" } }"

        val content : String = Jsoup.connect(url).get().body().text()
        val ret = Json.decodeFromString<JsonObject>(content)

        val length = ret["length"].run { Integer.parseInt("$this") }
        return List(length){ ret[ codes[it] ].toString() }
    }
}