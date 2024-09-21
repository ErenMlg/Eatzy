package com.softcross.eatzy.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


class CreditCardDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 5) text.text.substring(0..4) else text.text
        var out = ""

        for (i in trimmed.indices) {
            if (i == 2) out += "/"
            out += trimmed[i]
        }
        return TransformedText(AnnotatedString(out), cardDateTranslator)
    }

    val cardDateTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset >= 3) return offset + 1
            return offset
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset >= 3) return offset - 1
            return offset
        }
    }
}