package com.xxxsarutahikoxxx.kotli.janglish.structure

import kotlinx.serialization.Serializable

@Serializable
data class Sentence (
        val sentence : String,
        val translated : String,
        val vocabulary : List<String>,
        val quoted : String,
        val section : Int,
        val index : Int,
        val audio : String
){
    val shuffled : List<String> get(){
        return sentence.replace(Regex("[.,]"), "")
                .toLowerCase()
                .split(" ")
                .shuffled()
    }
    fun match(words : List<String>) : Boolean {
        return sentence.replace(Regex("[.,]"), "")
                .toLowerCase()
                .split(" ") == words
    }
}

/*
    {
        "sentence" : "We must respect the will of the individual." ,
        "translated" : "個人の意思は尊重しなければならない。" ,
        "vocabulary" : [ "respect", "will", "individual" ] ,
        "quoted" : "Duo 3.0" ,
        "section" : 1 ,
        "index" : 1 ,
        "audio" : "Duo 3.0 001.mp3"
    }
 */