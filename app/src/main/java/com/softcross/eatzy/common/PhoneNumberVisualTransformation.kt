package com.softcross.eatzy.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
class NanpVisualTransformation() : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text

        var out = if (trimmed.isNotEmpty()) "0(" else ""

        for (i in trimmed.indices) {
            if (i == 3) out += ") "
            if (i == 6) out += " "
            if (i == 8) out += " "
            out += trimmed[i]
        }
        return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
    }

    private val phoneNumberOffsetTranslator = object : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int =
            when (offset) {
                0 -> offset
                // Add 1 for opening parenthesis.
                in 1..3 -> offset + 2
                // Add 3 for both parentheses and a space.
                in 4..6 -> offset + 4
                // Add 4 for both parentheses, space, and hyphen.
                in 7..8 -> offset + 5
                else -> offset + 6
            }

        override fun transformedToOriginal(offset: Int): Int =
            when (offset) {
                0 -> offset
                1 -> offset -1
                // Subtract 1 for opening parenthesis.
                in 2..5 -> offset - 2
                // Subtract 3 for both parentheses and a space.
                in 6..10 -> offset - 4
                // Subtract 4 for both parentheses, space, and hyphen.
                in 11..12 -> offset - 5
                else -> offset - 6
            }

    }
}
