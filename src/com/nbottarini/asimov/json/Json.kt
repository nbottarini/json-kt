package com.nbottarini.asimov.json

import com.nbottarini.asimov.json.parser.JsonParser
import com.nbottarini.asimov.json.values.*
import java.io.Reader

object Json {
    val NULL: JsonValue = JsonLiteral.NULL
    val TRUE: JsonValue = JsonLiteral.TRUE
    val FALSE: JsonValue = JsonLiteral.FALSE

    fun value(value: Any?): JsonValue {
        when (value) {
            null -> return NULL
            is JsonValue -> return value
            is Int -> return value(value)
            is Long -> return value(value)
            is Float -> return value(value)
            is Double -> return value(value)
            is Boolean -> return value(value)
            is String -> return value(value)
            is List<*> -> return JsonArray(value.map { value(it) })
            is Map<*, *> -> {
                val json = JsonObject()
                value.forEach { (key, value) ->
                    json[key as String] = value(value)
                }
                return json
            }
            else -> throw UnsupportedOperationException("Unsupported type ${value.javaClass.simpleName}")
        }
    }

    fun value(value: Int) = JsonNumber(value.toString(10))

    fun value(value: Long) = JsonNumber(value.toString(10))

    fun value(value: Float): JsonValue {
        require(!value.isInfinite() && !value.isNaN()) { "Infinite and NaN values not permitted in JSON" }
        return JsonNumber(cutOffPointZero(value.toString()))
    }

    fun value(value: Double): JsonValue {
        require(!value.isInfinite() && !value.isNaN()) { "Infinite and NaN values not permitted in JSON" }
        return JsonNumber(cutOffPointZero(value.toString()))
    }

    fun value(value: Boolean) = if (value) TRUE else FALSE

    fun value(string: String?) = string?.let { JsonString(it) } ?: NULL

    fun array() = JsonArray()

    fun array(values: List<Any?>): JsonArray {
        val array = JsonArray()
        values.forEach { array.add(value(it)) }
        return array
    }

    fun array(vararg values: Int) = array(values.toList())

    fun array(vararg values: Long) = array(values.toList())

    fun array(vararg values: Float) = array(values.toList())

    fun array(vararg values: Double) = array(values.toList())

    fun array(vararg values: Boolean) = array(values.toList())

    fun array(vararg values: String?) = array(values.toList())

    fun array(vararg values: JsonValue) = array(values.toList())

    fun array(vararg values: Any?) = array(values.toList())

    fun obj() = JsonObject()

    fun obj(pairs: List<Pair<String, Any?>>) = JsonObject(pairs)

    fun obj(vararg pairs: Pair<String, Any?>) = JsonObject(pairs.toList())

    fun parse(string: String) = JsonParser().parse(string)

    fun parse(reader: Reader) = JsonParser().parse(reader)

    private fun cutOffPointZero(string: String): String {
        return if (string.endsWith(".0")) {
            string.substring(0, string.length - 2)
        } else string
    }
}
