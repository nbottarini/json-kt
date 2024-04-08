package dev.botta.json.writer

import java.io.Writer

open class MinimalJsonWriter(private val writer: Writer): JsonWriter {
    private val jsonStringWriter = JsonStringWriter()

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
        writer.write('['.code)
    }

    override fun writeArrayClose() {
        writer.write(']'.code)
    }

    override fun writeArraySeparator() {
        writer.write(','.code)
    }

    override fun writeObjectOpen() {
        writer.write('{'.code)
    }

    override fun writeObjectClose() {
        writer.write('}'.code)
    }

    override fun writeMemberSeparator() {
        writer.write(':'.code)
    }

    override fun writeObjectSeparator() {
        writer.write(','.code)
    }
}
