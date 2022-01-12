package com.nbottarini.asimov.json.values

import com.nbottarini.asimov.json.Json
import com.nbottarini.asimov.json.writer.JsonWriter
import com.nbottarini.asimov.json.writer.MinimalJsonWriter

class JsonArray(values: List<Any?> = listOf()): JsonValue(), MutableList<JsonValue> {
    private val values = mutableListOf<JsonValue>()
    override val isArray get() = true
    override val size get() = values.size

    constructor(vararg values: Any?): this(values.toList())

    init {
        if (values is JsonArray) {
            this.values.add(values)
        } else {
            this.values.addAll(values.map { Json.value(it) })
        }
    }

    override fun add(element: JsonValue) = values.add(element)

    fun add(element: Int) = add(Json.value(element))

    fun add(element: Long) = add(Json.value(element))

    fun add(element: Float) = add(Json.value(element))

    fun add(element: Double) = add(Json.value(element))

    fun add(element: Boolean) = add(Json.value(element))

    fun add(element: String?) = add(Json.value(element))

    override fun add(index: Int, element: JsonValue) {
        values.add(index, element)
    }

    fun add(index: Int, element: Int) {
        values.add(index, Json.value(element))
    }

    fun add(index: Int, element: Long) {
        values.add(index, Json.value(element))
    }

    fun add(index: Int, element: Float) {
        values.add(index, Json.value(element))
    }

    fun add(index: Int, element: Double) {
        values.add(index, Json.value(element))
    }

    fun add(index: Int, element: Boolean) {
        values.add(index, Json.value(element))
    }

    fun add(index: Int, element: String?) {
        values.add(index, Json.value(element))
    }

    override fun addAll(index: Int, elements: Collection<JsonValue>) = values.addAll(index, elements)

    @JvmName("addAllAny")
    fun addAll(index: Int, elements: Collection<Any?>) = values.addAll(index, elements.map { Json.value(it) })

    override fun addAll(elements: Collection<JsonValue>) = values.addAll(elements)

    @JvmName("addAllAny")
    fun addAll(elements: Collection<Any?>) = addAll(elements.map { Json.value(it) })

    override fun set(index: Int, element: JsonValue) = values.set(index, element)

    operator fun set(index: Int, element: Int) = values.set(index, Json.value(element))

    operator fun set(index: Int, element: Long) = values.set(index, Json.value(element))

    operator fun set(index: Int, element: Float) = values.set(index, Json.value(element))

    operator fun set(index: Int, element: Double) = values.set(index, Json.value(element))

    operator fun set(index: Int, element: Boolean) = values.set(index, Json.value(element))

    operator fun set(index: Int, element: String?) = values.set(index, Json.value(element))

    fun with(vararg elements: JsonValue) = apply { addAll(elements) }

    fun with(vararg elements: Any?) = apply { elements.forEach { add(Json.value(it)) } }

    fun with(vararg elements: Int) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Long) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Float) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Double) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Boolean) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: String?) = apply { elements.forEach { add(it) } }

    override fun remove(element: JsonValue) = values.remove(element)

    fun remove(element: Int) = remove(Json.value(element))

    fun remove(element: Long) = remove(Json.value(element))

    fun remove(element: Float) = remove(Json.value(element))

    fun remove(element: Double) = remove(Json.value(element))

    fun remove(element: Boolean) = remove(Json.value(element))

    fun remove(element: String?) = remove(Json.value(element))

    override fun removeAll(elements: Collection<JsonValue>) = values.removeAll(elements)

    @JvmName("removeAllAny")
    fun removeAll(elements: Collection<Any?>) = removeAll(elements.map { Json.value(it) })

    override fun removeAt(index: Int) = values.removeAt(index)

    override fun retainAll(elements: Collection<JsonValue>) = values.retainAll(elements)

    @JvmName("retainAllAny")
    fun retainAll(elements: Collection<Any?>) = retainAll(elements.map { Json.value(it) })

    override fun clear() {
        values.clear()
    }

    override fun asArray() = this

    override fun contains(element: JsonValue) = values.contains(element)

    fun contains(element: Int) = contains(Json.value(element))

    fun contains(element: Long) = contains(Json.value(element))

    fun contains(element: Float) = contains(Json.value(element))

    fun contains(element: Double) = contains(Json.value(element))

    fun contains(element: Boolean) = contains(Json.value(element))

    fun contains(element: String?) = contains(Json.value(element))

    override fun containsAll(elements: Collection<JsonValue>) = values.containsAll(elements)

    @JvmName("containsAllAny")
    fun containsAll(elements: Collection<Any?>) = containsAll(elements.map { Json.value(it) })

    override fun indexOf(element: JsonValue) = values.indexOf(element)

    fun indexOf(element: Int) = indexOf(Json.value(element))

    fun indexOf(element: Long) = indexOf(Json.value(element))

    fun indexOf(element: Float) = indexOf(Json.value(element))

    fun indexOf(element: Double) = indexOf(Json.value(element))

    fun indexOf(element: String?) = indexOf(Json.value(element))

    fun indexOf(element: Boolean) = indexOf(Json.value(element))

    override fun isEmpty() = values.isEmpty()

    override fun lastIndexOf(element: JsonValue) = values.lastIndexOf(element)

    fun lastIndexOf(element: Int) = lastIndexOf(Json.value(element))

    fun lastIndexOf(element: Long) = lastIndexOf(Json.value(element))

    fun lastIndexOf(element: Float) = lastIndexOf(Json.value(element))

    fun lastIndexOf(element: Double) = lastIndexOf(Json.value(element))

    fun lastIndexOf(element: String?) = lastIndexOf(Json.value(element))

    fun lastIndexOf(element: Boolean) = lastIndexOf(Json.value(element))

    override fun iterator() = values.iterator()

    override fun listIterator() = values.listIterator()

    override fun listIterator(index: Int) = values.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = values.subList(fromIndex, toIndex)

    override fun get(index: Int) = values[index]

    override fun write(writer: JsonWriter) {
        writer.writeArrayOpen()
        values.forEachIndexed { index, element ->
            element.write(writer)
            if (index != values.lastIndex) {
                writer.writeArraySeparator()
            }
        }
        writer.writeArrayClose()
    }

    override fun path(path: String): JsonValue? {
        if (!path.startsWith("[")) throw IllegalArgumentException("Invalid path $path")
        val endIndex = path.indexOf(']')
        if (endIndex == -1) throw IllegalArgumentException("Invalid path $path")
        val arrayIndex = path.substring(1, endIndex).toIntOrNull() ?: throw IllegalArgumentException("Invalid path $path")
        val value = get(arrayIndex)
        if (endIndex == path.lastIndex) return value
        return value.path(path.substring(endIndex + 1))
    }

    override fun hashCode() = values.hashCode()

    override fun equals(other: Any?) = other is JsonArray && other.values == values
}
