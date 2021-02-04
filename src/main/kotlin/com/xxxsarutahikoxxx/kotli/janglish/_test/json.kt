package com.xxxsarutahikoxxx.kotli.janglish._test

import com.xxxsarutahikoxxx.kotli.janglish.structure.Sentence
import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    val sentences : List<Sentence> = Json {  }.decodeFromString(
        """
[
{
"sentence" : "We must respect the will of the individual." ,
"translated" : "個人の意志は尊重しなければいけない。" ,
"vocabulary" : [ "respect ...", "will", "individual" ] ,
"quoted" : "Duo 3.0" ,
"section" : 1 ,
"index" : 1 ,
"audio" : "Duo 3.0 Sentence 001.mp3"
},
{
"sentence" : "Take it easy. I can assure you that everything will turn out fine." ,
"translated" : "気楽にいけよ。大丈夫、すべてうまくいくさ。" ,
"vocabulary" : [ "take it easy", "assure A (that)", "turn out (to be) ..." ] ,
"quoted" : "Duo 3.0" ,
"section" : 1 ,
"index" : 2 ,
"audio" : "Duo 3.0 Sentence 002.mp3"
}
]
        """.trimIndent()
        /*ClassLoader.getSystemResourceAsStream("").bufferedReader().readLines().joinToString("\n")*/
    )


    sentences.forEach {
        out = it.sentence
        out = it.vocabulary
    }

}