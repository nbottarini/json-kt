package dev.botta.json.values

import dev.botta.json.writer.JsonWriter

class JsonArray(values: List<Any?> = listOf()): JsonValue(), MutableList<JsonValue> {
    private val values = mutableListOf<JsonValue>()
    override val isArray get() = true
    override val size get() = values.size

    constructor(vararg values: Any?): this(values.toList())

    init {
        if (values is JsonArray) {
            this.values.add(values)
        } else {
            this.values.addAll(values.map { dev.botta.json.Json.value(it) })
        }
    }

    override fun add(element: JsonValue) = values.add(element)

    fun add(element: Int) = add(dev.botta.json.Json.value(element))

    fun add(element: Long) = add(dev.botta.json.Json.value(element))

    fun add(element: Float) = add(dev.botta.json.Json.value(element))

    fun add(element: Double) = add(dev.botta.json.Json.value(element))

    fun add(element: Boolean) = add(dev.botta.json.Json.value(element))

    fun add(element: String?) = add(dev.botta.json.Json.value(element))

    override fun add(index: Int, element: JsonValue) {
        values.add(index, element)
    }

    fun add(index: Int, element: Int) {
        values.add(index, dev.botta.json.Json.value(element))
    }

    fun add(index: Int, element: Long) {
        values.add(index, dev.botta.json.Json.value(element))
    }

    fun add(index: Int, element: Float) {
        values.add(index, dev.botta.json.Json.value(element))
    }

    fun add(index: Int, element: Double) {
        values.add(index, dev.botta.json.Json.value(element))
    }

    fun add(index: Int, element: Boolean) {
        values.add(index, dev.botta.json.Json.value(element))
    }

    fun add(index: Int, element: String?) {
        values.add(index, dev.botta.json.Json.value(element))
    }

    override fun addAll(index: Int, elements: Collection<JsonValue>) = values.addAll(index, elements)

    @JvmName("addAllAny")
    fun addAll(index: Int, elements: Collection<Any?>) = values.addAll(index, elements.map { dev.botta.json.Json.value(it) })

    override fun addAll(elements: Collection<JsonValue>) = values.addAll(elements)

    @JvmName("addAllAny")
    fun addAll(elements: Collection<Any?>) = addAll(elements.map { dev.botta.json.Json.value(it) })

    override fun set(index: Int, element: JsonValue) = values.set(index, element)

    operator fun set(index: Int, element: Int) = values.set(index, dev.botta.json.Json.value(element))

    operator fun set(index: Int, element: Long) = values.set(index, dev.botta.json.Json.value(element))

    operator fun set(index: Int, element: Float) = values.set(index, dev.botta.json.Json.value(element))

    operator fun set(index: Int, element: Double) = values.set(index, dev.botta.json.Json.value(element))

    operator fun set(index: Int, element: Boolean) = values.set(index, dev.botta.json.Json.value(element))

    operator fun set(index: Int, element: String?) = values.set(index, dev.botta.json.Json.value(element))

    fun with(vararg elements: JsonValue) = apply { addAll(elements) }

    fun with(vararg elements: Any?) = apply { elements.forEach { add(dev.botta.json.Json.value(it)) } }

    fun with(vararg elements: Int) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Long) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Float) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Double) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: Boolean) = apply { elements.forEach { add(it) } }

    fun with(vararg elements: String?) = apply { elements.forEach { add(it) } }

    override fun remove(element: JsonValue) = values.remove(element)

    fun remove(element: Int) = remove(dev.botta.json.Json.value(element))

    fun remove(element: Long) = remove(dev.botta.json.Json.value(element))

    fun remove(element: Float) = remove(dev.botta.json.Json.value(element))

    fun remove(element: Double) = remove(dev.botta.json.Json.value(element))

    fun remove(element: Boolean) = remove(dev.botta.json.Json.value(element))

    fun remove(element: String?) = remove(dev.botta.json.Json.value(element))

    override fun removeAll(elements: Collection<JsonValue>) = values.removeAll(elements)

    @JvmName("removeAllAny")
    fun removeAll(elements: Collection<Any?>) = removeAll(elements.map { dev.botta.json.Json.value(it) })

    override fun removeAt(index: Int) = values.removeAt(index)

    override fun retainAll(elements: Collection<JsonValue>) = values.retainAll(elements)

    @JvmName("retainAllAny")
    fun retainAll(elements: Collection<Any?>) = retainAll(elements.map { dev.botta.json.Json.value(it) })

    override fun clear() {
        values.clear()
    }

    override fun asArray() = this

    override fun contains(element: JsonValue) = values.contains(element)

    fun contains(element: Int) = contains(dev.botta.json.Json.value(element))

    fun contains(element: Long) = contains(dev.botta.json.Json.value(element))

    fun contains(element: Float) = contains(dev.botta.json.Json.value(element))

    fun contains(element: Double) = contains(dev.botta.json.Json.value(element))

    fun contains(element: Boolean) = contains(dev.botta.json.Json.value(element))

    fun contains(element: String?) = contains(dev.botta.json.Json.value(element))

    override fun containsAll(elements: Collection<JsonValue>) = values.containsAll(elements)

    @JvmName("containsAllAny")
    fun containsAll(elements: Collection<Any?>) = containsAll(elements.map { dev.botta.json.Json.value(it) })

    override fun indexOf(element: JsonValue) = values.indexOf(element)

    fun indexOf(element: Int) = indexOf(dev.botta.json.Json.value(element))

    fun indexOf(element: Long) = indexOf(dev.botta.json.Json.value(element))

    fun indexOf(element: Float) = indexOf(dev.botta.json.Json.value(element))

    fun indexOf(element: Double) = indexOf(dev.botta.json.Json.value(element))

    fun indexOf(element: String?) = indexOf(dev.botta.json.Json.value(element))

    fun indexOf(element: Boolean) = indexOf(dev.botta.json.Json.value(element))

    override fun isEmpty() = values.isEmpty()

    override fun lastIndexOf(element: JsonValue) = values.lastIndexOf(element)

    fun lastIndexOf(element: Int) = lastIndexOf(dev.botta.json.Json.value(element))

    fun lastIndexOf(element: Long) = lastIndexOf(dev.botta.json.Json.value(element))

    fun lastIndexOf(element: Float) = lastIndexOf(dev.botta.json.Json.value(element))

    fun lastIndexOf(element: Double) = lastIndexOf(dev.botta.json.Json.value(element))

    fun lastIndexOf(element: String?) = lastIndexOf(dev.botta.json.Json.value(element))

    fun lastIndexOf(element: Boolean) = lastIndexOf(dev.botta.json.Json.value(element))

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
