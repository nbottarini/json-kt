package dev.botta.json.writer

import java.io.Writer
import java.util.*

class PrettyPrint private constructor(private val indentChars: CharArray?): WriterConfig() {
    override fun createWriter(writer: Writer) = PrettyPrintWriter(writer, indentChars)

    companion object {
        fun singleLine() = PrettyPrint(null)

        fun indentWithSpaces(number: Int): PrettyPrint {
            require(number >= 0) { "number is negative" }
            val chars = CharArray(number)
            Arrays.fill(chars, ' ')
            return PrettyPrint(chars)
        }

        fun indentWithTabs() = PrettyPrint(charArrayOf('\t'))
    }
}
