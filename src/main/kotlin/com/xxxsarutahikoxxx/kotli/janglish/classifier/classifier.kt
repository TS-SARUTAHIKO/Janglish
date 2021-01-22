package com.xxxsarutahikoxxx.kotli.janglish.classifier

import com.xxxsarutahikoxxx.kotli.janglish.structure.Vocabulary

interface Classifier {
    operator fun contains(spell : String) : Boolean
    operator fun contains(vocabulary: Vocabulary) : Boolean = contains(vocabulary.spell)
}
