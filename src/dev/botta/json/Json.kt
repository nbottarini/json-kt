package dev.botta.json

import dev.botta.json.parser.JsonParser
import dev.botta.json.values.*
import java.io.Reader

object Json {
    val NULL: JsonValue = JsonLiteral.NULL
    val TRUE: JsonValue = JsonLiteral.TRUE
    val FALSE: JsonValue = JsonLiteral.FALSE

    fun value(value: Any?): JsonValue {
        when (value) {
            null -> return dev.botta.json.Json.NULL
            is JsonValue -> return value
            is Int -> return dev.botta.json.Json.value(value)
            is Long -> return dev.botta.json.Json.value(value)
            is Float -> return dev.botta.json.Json.value(value)
            is Double -> return dev.botta.json.Json.value(value)
            is Boolean -> return dev.botta.json.Json.value(value)
            is String -> return dev.botta.json.Json.value(value)
            is List<*> -> return JsonArray(value.map { dev.botta.json.Json.value(it) })
            is Map<*, *> -> {
                val json = JsonObject()
                value.forEach { (key, value) ->
                    json[key as String] = dev.botta.json.Json.value(value)
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
        return JsonNumber(dev.botta.json.Json.cutOffPointZero(value.toString()))
    }

    fun value(value: Double): JsonValue {
        require(!value.isInfinite() && !value.isNaN()) { "Infinite and NaN values not permitted in JSON" }
        return JsonNumber(dev.botta.json.Json.cutOffPointZero(value.toString()))
    }

    fun value(value: Boolean) = if (value) dev.botta.json.Json.TRUE else dev.botta.json.Json.FALSE

    fun value(string: String?) = string?.let { JsonString(it) } ?: dev.botta.json.Json.NULL

    fun array() = JsonArray()

    fun array(values: List<Any?>): JsonArray {
        val array = JsonArray()
        values.forEach { array.add(dev.botta.json.Json.value(it)) }
        return array
    }

    fun array(vararg values: Int) = dev.botta.json.Json.array(values.toList())

    fun array(vararg values: Long) = dev.botta.json.Json.array(values.toList())

    fun array(vararg values: Float) = dev.botta.json.Json.array(values.toList())

    fun array(vararg values: Double) = dev.botta.json.Json.array(values.toList())

    fun array(vararg values: Boolean) = dev.botta.json.Json.array(values.toList())

    fun array(vararg values: String?) = dev.botta.json.Json.array(values.toList())

    fun array(vararg values: JsonValue) = dev.botta.json.Json.array(values.toList())

    fun array(vararg values: Any?) = dev.botta.json.Json.array(values.toList())

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
