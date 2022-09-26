package com.nbottarini.asimov.json.values

import com.nbottarini.asimov.json.Json
import com.nbottarini.asimov.json.writer.JsonWriter

class JsonObject(pairs: List<Pair<String, Any?>> = listOf()): JsonValue(), MutableMap<String, JsonValue> {
    private val map = mutableMapOf<String, JsonValue>()
    override val size get() = map.size
    override val entries get() = map.entries
    override val keys get() = map.keys
    override val values get() = map.values
    override val isObject get() = true

    constructor(vararg pairs: Pair<String, Any?>): this(pairs.toList())

    init {
        pairs.forEach { map[it.first] = Json.value(it.second) }
    }

    fun with(key: String, value: Int) = apply { with(key, Json.value(value)) }

    fun with(key: String, value: Long) = apply { with(key, Json.value(value)) }

    fun with(key: String, value: Float) = apply { with(key, Json.value(value)) }

    fun with(key: String, value: Double) = apply { with(key, Json.value(value)) }

    fun with(key: String, value: Boolean) = apply { with(key, Json.value(value)) }

    fun with(key: String, value: String?) = apply { with(key, Json.value(value)) }

    fun with(key: String, value: Any?) = apply { with(key, Json.value(value)) }

    fun with(key: String, value: JsonValue) = apply { map[key] = value }

    fun with(vararg pairs: Pair<String, Any?>) = apply { pairs.forEach { with(it.first, it.second) } }

    operator fun set(key: String, value: Int) = set(key, Json.value(value))

    operator fun set(key: String, value: Long) = set(key, Json.value(value))

    operator fun set(key: String, value: Float) = set(key, Json.value(value))

    operator fun set(key: String, value: Double) = set(key, Json.value(value))

    operator fun set(key: String, value: Boolean) = set(key, Json.value(value))

    operator fun set(key: String, value: String?) = set(key, Json.value(value))

    operator fun set(key: String, value: JsonValue) = map.put(key, value)

    fun merge(obj: JsonObject) {
        putAll(obj)
    }

    override fun get(key: String) = map[key]

    override fun path(path: String): JsonValue? {
        val startIndex = if (path.startsWith(".")) 1 else 0
        var endIndex = path.indexOfAny(arrayOf('.', '[').toCharArray(), startIndex)
        if (endIndex == -1) endIndex = path.length
        val key = path.substring(startIndex, endIndex)
        if (key.isBlank()) throw IllegalArgumentException("Invalid path $path")
        val value = get(key) ?: return null
        if (endIndex == path.length) return value
        return value.path(path.substring(endIndex))
    }

    override fun write(writer: JsonWriter) {
        writer.writeObjectOpen()
        val namesIterator: Iterator<String> = keys.iterator()
        val valuesIterator: Iterator<JsonValue> = values.iterator()
        if (namesIterator.hasNext()) {
            writer.writeString(namesIterator.next())
            writer.writeMemberSeparator()
            valuesIterator.next().write(writer)
            while (namesIterator.hasNext()) {
                writer.writeObjectSeparator()
                writer.writeString(namesIterator.next())
                writer.writeMemberSeparator()
                valuesIterator.next().write(writer)
            }
        }
        writer.writeObjectClose()
    }

    override fun asObject() = this

    override fun containsKey(key: String) = map.containsKey(key)

    override fun containsValue(value: JsonValue) = map.containsValue(value)

    override fun isEmpty() = map.isEmpty()

    override fun clear() {
        map.clear()
    }

    override fun put(key: String, value: JsonValue) = map.put(key, value)

    override fun putAll(from: Map<out String, JsonValue>) {
        map.putAll(from)
    }

    override fun remove(key: String) = map.remove(key)

    override fun hashCode() = map.hashCode()

    override fun equals(other: Any?) = other is JsonObject && other.map == map

    fun getStringOrThrow(key: String) = get(key)?.asString() ?: throw IllegalAccessError("$key not found or is not a String")

    fun getIntOrThrow(key: String) = get(key)?.asInt() ?: throw IllegalAccessError("$key not found or is not an Int")

    fun getLongOrThrow(key: String) = get(key)?.asLong() ?: throw IllegalAccessError("$key not found or is not an Long")

    fun getFloatOrThrow(key: String) = get(key)?.asFloat() ?: throw IllegalAccessError("$key not found or is not an Float")

    fun getDoubleOrThrow(key: String) = get(key)?.asDouble() ?: throw IllegalAccessError("$key not found or is not an Double")

    fun getBooleanOrThrow(key: String) = get(key)?.asBoolean() ?: throw IllegalAccessError("$key not found or is not an Boolean")

    fun getObjectOrThrow(key: String) = get(key)?.asObject() ?: throw IllegalAccessError("$key not found or is not an JsonObject")

    fun getArrayOrThrow(key: String) = get(key)?.asArray() ?: throw IllegalAccessError("$key not found or is not an JsonArray")
}
