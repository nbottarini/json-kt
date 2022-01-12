package com.nbottarini.asimov.json.writer

import java.io.Writer
import java.util.*

class PrettyPrintWriter(private val writer: Writer, private val indentChars: CharArray?): JsonWriter {
    private val jsonStringWriter = JsonStringWriter()
    private var indent = 0

    override fun writeLiteral(value: String) {
        writer.write(value)
    }

    override fun writeNumber(string: String) {
        writer.write(string)
    }

    override fun writeString(string: String) {
        writer.write('"'.code)
        jsonStringWriter.write(string, writer)
        writer.write('"'.code)
    }

    override fun writeArrayOpen() {
        indent++
        writer.write('['.code)
        writeNewLine()
    }

    override fun writeArrayClose() {
        indent--
        writeNewLine()
        writer.write(']'.code)
    }

    override fun writeArraySeparator() {
        writer.write(','.code)
        if (!writeNewLine()) {
            writer.write(' '.code)
        }
    }

    override fun writeObjectOpen() {
        indent++
        writer.write('{'.code)
        writeNewLine()
    }

    override fun writeObjectClose() {
        indent--
        writeNewLine()
        writer.write('}'.code)
    }

    override fun writeMemberSeparator() {
        writer.write(':'.code)
        writer.write(' '.code)
    }

    override fun writeObjectSeparator() {
        writer.write(','.code)
        if (!writeNewLine()) {
            writer.write(' '.code)
        }
    }

    private fun writeNewLine(): Boolean {
        if (indentChars == null) {
            return false
        }
        writer.write('\n'.code)
        for (i in 0 until indent) {
            writer.write(indentChars)
        }
        return true
    }
}
