package com.xxxsarutahikoxxx.kotli.janglish.structure

import kotlinx.serialization.Serializable

@Serializable
data class PhoneticSymbol(
    val symbol : String,
    val subSymbol : String? = null,
    val howToPronounce : String,
    val tips : String,
    val examples : List<String>
) {


}
