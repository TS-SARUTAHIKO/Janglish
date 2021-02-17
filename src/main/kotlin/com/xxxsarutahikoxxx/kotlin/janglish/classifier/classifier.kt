package com.xxxsarutahikoxxx.kotlin.janglish.classifier

import com.xxxsarutahikoxxx.kotlin.janglish.structure.Vocabulary

interface Classifier {
    operator fun contains(spell : String) : Boolean
    operator fun contains(vocabulary: Vocabulary) : Boolean = contains(vocabulary.spell)
}
