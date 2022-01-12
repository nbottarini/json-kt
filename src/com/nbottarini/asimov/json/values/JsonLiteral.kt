package com.nbottarini.asimov.json.values

import com.nbottarini.asimov.json.writer.JsonWriter
import com.nbottarini.asimov.json.writer.MinimalJsonWriter

class JsonLiteral internal constructor(private val value: String): JsonValue() {
    override val isNull = value == "null"
    override val isTrue = value == "true"
    override val isFalse = value == "false"
    override val isBoolean get() = isTrue || isFalse

    override fun asBoolean(): Boolean? {
        if (isNull) return null
        return isTrue
    }

    override fun equals(other: Any?) = other is JsonLiteral && other.value == value

    override fun hashCode() = value.hashCode()

    override fun toString() = value

    override fun write(writer: JsonWriter) {
        writer.writeLiteral(value)
    }

    companion object {
        val NULL = JsonLiteral("null")
        val TRUE = JsonLiteral("true")
        val FALSE = JsonLiteral("false")
    }
}
