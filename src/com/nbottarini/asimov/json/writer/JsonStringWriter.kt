package com.nbottarini.asimov.json.writer

import java.io.Writer

internal class JsonStringWriter {
    fun write(value: String, writer: Writer) {
        val length = value.length
        var start = 0
        for (index in 0 until length) {
            val replacement = getReplacementChars(value[index])
            if (replacement != null) {
                writer.write(value, start, index - start)
                writer.write(replacement)
                start = index + 1
            }
        }
        writer.write(value, start, length - start)
    }

    companion object {
        private const val CONTROL_CHARACTERS_END = 0x001f
        private val QUOT_CHARS = charArrayOf('\\', '"')
        private val BS_CHARS = charArrayOf('\\', '\\')
        private val LF_CHARS = charArrayOf('\\', 'n')
        private val CR_CHARS = charArrayOf('\\', 'r')
        private val TAB_CHARS = charArrayOf('\\', 't')

        // In JavaScript, U+2028 and U+2029 characters count as line endings and must be encoded.
        // http://stackoverflow.com/questions/2965293/javascript-parse-error-on-u2028-unicode-character
        private val UNICODE_2028_CHARS = charArrayOf('\\', 'u', '2', '0', '2', '8')
        private val UNICODE_2029_CHARS = charArrayOf('\\', 'u', '2', '0', '2', '9')
        private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f')

        private fun getReplacementChars(ch: Char): CharArray? {
            if (ch > '\\') {
                if (ch < '\u2028' || ch > '\u2029') {
                    // The lower range contains 'a' .. 'z'. Only 2 checks required.
                    return null
                }
                return if (ch == '\u2028') UNICODE_2028_CHARS else UNICODE_2029_CHARS
            }
            if (ch == '\\') return BS_CHARS
            if (ch > '"') {
                // This range contains '0' .. '9' and 'A' .. 'Z'. Need 3 checks to get here.
                return null
            }
            if (ch == '"') return QUOT_CHARS
            if (ch.code > CONTROL_CHARACTERS_END) return null
            if (ch == '\n') return LF_CHARS
            if (ch == '\r') return CR_CHARS
            return if (ch == '\t') {
                TAB_CHARS
            } else charArrayOf('\\', 'u', '0', '0', HEX_DIGITS[ch.code shr 4 and 0x000f],
                HEX_DIGITS[ch.code and 0x000f])
        }
    }
}
