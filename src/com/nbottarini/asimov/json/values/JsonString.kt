package com.nbottarini.asimov.json.values

import com.nbottarini.asimov.json.writer.JsonWriter

class JsonString internal constructor (private val value: String): JsonValue() {
    override val isString = true

    override fun asString() = value

    override fun equals(other: Any?) = other is JsonString && other.value == value

    override fun hashCode() = value.hashCode()

    override fun write(writer: JsonWriter) {
        writer.writeString(value)
    }
}
