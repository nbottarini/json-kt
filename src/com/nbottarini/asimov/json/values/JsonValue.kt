package com.nbottarini.asimov.json.values

import com.nbottarini.asimov.json.writer.BufferedWriter
import com.nbottarini.asimov.json.writer.JsonWriter
import com.nbottarini.asimov.json.writer.PrettyPrint
import com.nbottarini.asimov.json.writer.WriterConfig
import java.io.Serializable
import java.io.StringWriter

abstract class JsonValue: Serializable {
    open val isObject get() = false
    open val isArray get() = false
    open val isNumber get() = false
    open val isString get() = false
    open val isBoolean get() = false
    open val isTrue get() = false
    open val isFalse get() = false
    open val isNull get() = false

    open fun asObject(): JsonObject? {
        return null
    }

    fun asObject(default: JsonObject) = asObject() ?: default

    open fun asArray(): JsonArray? {
        return null
    }

    fun asArray(default: JsonArray) = asArray() ?: default

    open fun asInt(): Int? {
        return null
    }

    fun asInt(default: Int) = asInt() ?: default

    open fun asLong(): Long? {
        return null
    }

    fun asLong(default: Long) = asLong() ?: default

    open fun asFloat(): Float? {
        return null
    }

    fun asFloat(default: Float) = asFloat() ?: default

    open fun asDouble(): Double? {
        return null
    }

    fun asDouble(default: Double) = asDouble() ?: default

    open fun asString(): String? {
        return null
    }

    fun asString(default: String) = asString() ?: default

    open fun asBoolean(): Boolean? {
        return null
    }

    fun asBoolean(default: Boolean) = asBoolean() ?: default

    open fun path(path: String): JsonValue? {
        if (path.isEmpty()) return this
        return null
    }

    override fun toString() = toString(WriterConfig.MINIMAL)

    fun toPrettyString() = toString(PrettyPrint.indentWithSpaces(2))

    fun toString(config: WriterConfig): String {
        val writer = StringWriter()
        val buffer = BufferedWriter(writer, 128)
        write(config.createWriter(buffer))
        buffer.flush()
        return writer.toString()
    }

    abstract fun write(writer: JsonWriter)
}
