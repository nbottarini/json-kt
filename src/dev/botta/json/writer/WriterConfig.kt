package dev.botta.json.writer

import java.io.Writer

abstract class WriterConfig {
    abstract fun createWriter(writer: Writer): JsonWriter

    companion object {
        var MINIMAL = object: WriterConfig() {
            override fun createWriter(writer: Writer) = MinimalJsonWriter(writer)
        }
    }
}
