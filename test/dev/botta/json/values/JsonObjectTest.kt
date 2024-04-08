@file:Suppress("ClassName")

package dev.botta.json.values

import dev.botta.json.TestUtil
import dev.botta.json.writer.MinimalJsonWriter
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

class JsonObjectTest {
    @Test
    fun `create with list of pairs`() {
        val obj = JsonObject(listOf("name" to "John", "age" to 54))

        assertThat(obj.size).isEqualTo(2)
        assertThat(obj["name"]).isEqualTo(dev.botta.json.Json.value("John"))
        assertThat(obj["age"]).isEqualTo(dev.botta.json.Json.value(54))
    }

    @Test
    fun `create with pairs`() {
        val obj = JsonObject("name" to "John", "age" to 54)

        assertThat(obj.size).isEqualTo(2)
        assertThat(obj["name"]).isEqualTo(dev.botta.json.Json.value("John"))
        assertThat(obj["age"]).isEqualTo(dev.botta.json.Json.value(54))
    }

    @Test
    fun isEmpty() {
        assertThat(JsonObject().isEmpty()).isTrue
        assertThat(JsonObject("foo" to 23).isEmpty()).isFalse
    }

    @Test
    fun size() {
        assertThat(JsonObject().size).isEqualTo(0)
        assertThat(JsonObject("a" to 1, "b" to 2).size).isEqualTo(2)
    }

    @Nested
    inner class iterator {
        @Test
        fun `iterator is empty after creation`() {
            assertThat(obj.iterator().hasNext()).isFalse
        }

        @Test
        fun `iterator next returns actual value`() {
            obj.with("a", 1)
            val iterator = obj.iterator()

            val item = iterator.next()
            assertThat(item.key).isEqualTo("a")
            assertThat(item.value).isEqualTo(dev.botta.json.Json.value(1))
        }

        @Test
        fun `iterator next progresses to next value`() {
            obj.with("a" to 1, "b" to 2)
            val iterator = obj.iterator()

            iterator.next()

            val item = iterator.next()
            assertThat(item.key).isEqualTo("b")
            assertThat(item.value).isEqualTo(dev.botta.json.Json.value(2))
        }

        @Test
        fun `iterator next fails at end`() {
            obj.with("a", 1)
            val iterator = obj.iterator()
            iterator.next()

            assertThrows<NoSuchElementException> { iterator.next() }
        }

        @Test
        fun `iterator fails if concurrent modification`() {
            val iterator = obj.iterator()
            obj["a"] = 1

            assertThrows<ConcurrentModificationException> { iterator.next() }
        }
    }

    @Test
    fun `get returns null for non existing member`() {
        assertThat(obj["foo"]).isNull()
    }

    @Test
    fun `get returns value for key`() {
        obj.with("foo", "bar")

        assertThat(obj["foo"]).isEqualTo(dev.botta.json.Json.value("bar"))
    }

    @Nested
    inner class with {
        @Test
        fun `with int`() {
            obj.with("a", 23)
            assertThat(obj).isEqualTo(JsonObject("a" to 23))
        }

        @Test
        fun `with long`() {
            obj.with("a", 23L)
            assertThat(obj).isEqualTo(JsonObject("a" to 23L))
        }

        @Test
        fun `with float`() {
            obj.with("a", 3.14f)
            assertThat(obj).isEqualTo(JsonObject("a" to 3.14f))
        }

        @Test
        fun `with double`() {
            obj.with("a", 3.14)
            assertThat(obj).isEqualTo(JsonObject("a" to 3.14))
        }

        @Test
        fun `with boolean`() {
            obj.with("a", true)
            assertThat(obj).isEqualTo(JsonObject("a" to true))
        }

        @Test
        fun `with string`() {
            obj.with("a", "foo")
            assertThat(obj).isEqualTo(JsonObject("a" to "foo"))
        }

        @Test
        fun `with nullable string`() {
            obj.with("a", null as String?)
            assertThat(obj).isEqualTo(JsonObject("a" to null))
        }

        @Test
        fun `with json null`() {
            obj.with("a", dev.botta.json.Json.NULL)
            assertThat(obj).isEqualTo(JsonObject("a" to dev.botta.json.Json.NULL))
        }

        @Test
        fun `with json array`() {
            obj.with("a", JsonArray())
            assertThat(obj).isEqualTo(JsonObject("a" to JsonArray()))
        }

        @Test
        fun `with json object`() {
            obj.with("a", JsonObject())
            assertThat(obj).isEqualTo(JsonObject("a" to JsonObject()))
        }

        @Test
        fun `with multiple params`() {
            obj.with("name" to "John", "age" to 23)
            assertThat(obj).isEqualTo(JsonObject("name" to "John", "age" to 23))
        }
    }

    @Nested
    inner class set {
        @Test
        fun `set int`() {
            obj["a"] = 23
            assertThat(obj).isEqualTo(JsonObject("a" to 23))
        }

        @Test
        fun `set long`() {
            obj["a"] = 23L
            assertThat(obj).isEqualTo(JsonObject("a" to 23L))
        }

        @Test
        fun `set float`() {
            obj["a"] = 3.14f
            assertThat(obj).isEqualTo(JsonObject("a" to 3.14f))
        }

        @Test
        fun `set double`() {
            obj["a"] = 3.14
            assertThat(obj).isEqualTo(JsonObject("a" to 3.14))
        }

        @Test
        fun `set boolean`() {
            obj["a"] = true
            assertThat(obj).isEqualTo(JsonObject("a" to true))
        }

        @Test
        fun `set string`() {
            obj["a"] = "foo"
            assertThat(obj).isEqualTo(JsonObject("a" to "foo"))
        }

        @Test
        fun `set nullable string`() {
            obj["a"] = null as String?
            assertThat(obj).isEqualTo(JsonObject("a" to null))
        }

        @Test
        fun `set json null`() {
            obj["a"] = dev.botta.json.Json.NULL
            assertThat(obj).isEqualTo(JsonObject("a" to dev.botta.json.Json.NULL))
        }

        @Test
        fun `set json array`() {
            obj["a"] = JsonArray()
            assertThat(obj).isEqualTo(JsonObject("a" to JsonArray()))
        }

        @Test
        fun `set json object`() {
            obj["a"] = JsonObject()
            assertThat(obj).isEqualTo(JsonObject("a" to JsonObject()))
        }

        @Test
        fun `set changes element`() {
            obj["a"] = 23
            obj["a"] = 42
            assertThat(obj).isEqualTo(JsonObject("a" to 42))
        }

        @Test
        fun put() {
            obj.put("a", dev.botta.json.Json.value("foo"))
            assertThat(obj).isEqualTo(JsonObject("a" to "foo"))
        }

        @Test
        fun `put changes element`() {
            obj.put("a", dev.botta.json.Json.value("23"))
            obj.put("a", dev.botta.json.Json.value("foo"))
            assertThat(obj).isEqualTo(JsonObject("a" to "foo"))
        }

        @Test
        fun putAll() {
            obj.with("a" to 1)
            obj.putAll(JsonObject("b" to 2, "c" to 3))
            assertThat(obj).isEqualTo(JsonObject("a" to 1, "b" to 2, "c" to 3))
        }
    }

    @Test
    fun remove() {
        obj.with("a" to 1, "b" to 2, "c" to 3)
        obj.remove("b")
        assertThat(obj).isEqualTo(JsonObject("a" to 1, "c" to 3))
    }

    @Test
    fun `remove doesnt fail if name doesnt exist`() {
        obj.with("a" to 1, "b" to 2, "c" to 3)
        assertDoesNotThrow { obj.remove("d") }
    }

    @Test
    fun clear() {
        obj.with("a" to 1, "b" to 2, "c" to 3)
        obj.clear()
        assertThat(obj.isEmpty()).isTrue
    }

    @Test
    fun containsKey() {
        obj.with("a" to 1, "b" to 2, "c" to 3)

        assertThat(obj.containsKey("a")).isTrue
        assertThat(obj.containsKey("c")).isTrue
        assertThat(obj.containsKey("d")).isFalse
    }

    @Test
    fun containsValue() {
        obj.with("a" to 1, "b" to 2, "c" to 3)

        assertThat(obj.containsValue(dev.botta.json.Json.value(2))).isTrue
        assertThat(obj.containsValue(dev.botta.json.Json.value(4))).isFalse
    }

    @Test
    fun entries() {
        obj.with("a" to 1, "b" to 2, "c" to 3)

        assertThat(obj.entries.size).isEqualTo(3)
        assertThat(obj.entries.any { it.key == "a" && it.value == dev.botta.json.Json.value(1) }).isTrue
        assertThat(obj.entries.any { it.key == "b" && it.value == dev.botta.json.Json.value(2) }).isTrue
        assertThat(obj.entries.any { it.key == "c" && it.value == dev.botta.json.Json.value(3) }).isTrue
    }

    @Test
    fun keys() {
        obj.with("a" to 1, "b" to 2, "c" to 3)

        assertThat(obj.keys).containsExactlyInAnyOrder("a", "b", "c")
    }

    @Test
    fun values() {
        obj.with("a" to 1, "b" to 2, "c" to 3)

        assertThat(obj.values).containsExactlyInAnyOrder(dev.botta.json.Json.value(1), dev.botta.json.Json.value(2), dev.botta.json.Json.value(3))
    }

    @Test
    fun `merge appends members`() {
        obj.with("a" to 1, "b" to 2)
        obj.merge(JsonObject("c" to 3, "d" to 4))

        assertThat(obj).isEqualTo(JsonObject("a" to 1, "b" to 2, "c" to 3, "d" to 4))
    }

    @Test
    fun `merge replaces matching members`() {
        obj.with("a" to 1, "b" to 2, "c" to 3)
        obj.merge(JsonObject("c" to 5, "d" to 4))

        assertThat(obj).isEqualTo(JsonObject("a" to 1, "b" to 2, "c" to 5, "d" to 4))
    }

    @Test
    fun `write empty`() {
        obj.write(writer)

        verifySequence {
            writer.writeObjectOpen()
            writer.writeObjectClose()
        }
        confirmVerified(writer)
    }

    @Test
    fun `write values`() {
        obj.with("a" to 1, "b" to "foo")

        obj.write(writer)

        verifySequence {
            writer.writeObjectOpen()
            writer.writeString("a")
            writer.writeMemberSeparator()
            writer.writeNumber("1")
            writer.writeObjectSeparator()
            writer.writeString("b")
            writer.writeMemberSeparator()
            writer.writeString("foo")
            writer.writeObjectClose()
        }
        confirmVerified(writer)
    }

    @Test
    fun equals() {
        assertThat(JsonObject()).isEqualTo(JsonObject())
        assertThat(JsonObject("a" to 1, "b" to 2)).isEqualTo(JsonObject("a" to 1, "b" to 2))
        assertThat(JsonObject("a" to 1, "b" to 2)).isNotEqualTo(JsonObject("a" to 1, "b" to 2, "c" to 3))
        assertThat(JsonObject("a" to 1, "b" to 2)).isNotEqualTo(JsonObject("a" to 1, "b" to 5))
    }

    @Test
    fun `to string`() {
        assertThat(JsonObject().toString()).isEqualTo("{}")
        assertThat(JsonObject("a" to 1).toString()).isEqualTo("{\"a\":1}")
        assertThat(JsonObject("a" to 3.14).toString()).isEqualTo("{\"a\":3.14}")
        assertThat(JsonObject("a" to "foo").toString()).isEqualTo("{\"a\":\"foo\"}")
        assertThat(JsonObject("a" to "foo", "b" to "bar").toString()).isEqualTo("{\"a\":\"foo\",\"b\":\"bar\"}")
        assertThat(JsonObject("a" to JsonArray(1, 2, 3)).toString()).isEqualTo("{\"a\":[1,2,3]}")
    }

    @Test
    fun `to string nested`() {
        assertThat(JsonObject("a" to JsonObject()).toString()).isEqualTo("{\"a\":{}}")
        assertThat(JsonObject("a" to JsonObject("b" to "foo")).toString()).isEqualTo("{\"a\":{\"b\":\"foo\"}}")
        assertThat(JsonObject("a" to JsonObject("b" to "foo"),"c" to JsonObject("b" to "bar")).toString())
            .isEqualTo("{\"a\":{\"b\":\"foo\"},\"c\":{\"b\":\"bar\"}}")
    }

    @Test
    fun `is serializable`() {
        obj.with("a" to 1, "b" to "foo", "c" to true)
        assertThat(TestUtil.serializeAndDeserialize(obj)).isEqualTo(obj)
    }

    @Nested
    inner class `type conversions` {
        @Test
        fun `type checking`() {
            assertThat(JsonObject().isObject).isTrue
            assertThat(JsonObject().isArray).isFalse
            assertThat(JsonObject().isNumber).isFalse
            assertThat(JsonObject().isString).isFalse
            assertThat(JsonObject().isBoolean).isFalse
            assertThat(JsonObject().isNull).isFalse
            assertThat(JsonObject().isTrue).isFalse
            assertThat(JsonObject().isFalse).isFalse
        }

        @Test
        fun asObject() {
            val default = dev.botta.json.Json.obj("name" to "John")
            assertThat(JsonObject().asObject()).isNotNull
            assertThat(JsonObject().asObject(default)).isNotEqualTo(default)
        }

        @Test
        fun asArray() {
            val default = dev.botta.json.Json.array(1, 2, 3)
            assertThat(JsonObject().asArray()).isNull()
            assertThat(JsonObject().asArray(default)).isEqualTo(default)
        }

        @Test
        fun asInt() {
            val default = 55
            assertThat(JsonObject().asInt()).isNull()
            assertThat(JsonObject().asInt(default)).isEqualTo(default)
        }

        @Test
        fun asLong() {
            val default = 55L
            assertThat(JsonObject().asLong()).isNull()
            assertThat(JsonObject().asLong(default)).isEqualTo(default)
        }

        @Test
        fun asFloat() {
            val default = 7.14f
            assertThat(JsonObject().asFloat()).isNull()
            assertThat(JsonObject().asFloat(default)).isEqualTo(default)
        }

        @Test
        fun asDouble() {
            val default = 7.14
            assertThat(JsonObject().asDouble()).isNull()
            assertThat(JsonObject().asDouble(default)).isEqualTo(default)
        }

        @Test
        fun asString() {
            val default = "bar"
            assertThat(JsonObject().asString()).isNull()
            assertThat(JsonObject().asString(default)).isEqualTo(default)
        }

        @Test
        fun asBoolean() {
            val default = false
            assertThat(JsonObject().asBoolean()).isNull()
            assertThat(JsonObject().asBoolean(default)).isEqualTo(default)
        }
    }

    @Test
    fun `path simple object`() {
        val json = JsonObject("name" to "John")
        assertThat(json.path("name")).isEqualTo(dev.botta.json.Json.value("John"))
        assertThat(json.path(".name")).isEqualTo(dev.botta.json.Json.value("John"))
    }

    @Test
    fun `path nested objects`() {
        val json = JsonObject("user" to JsonObject("address" to JsonObject("street" to "Avellaneda")))
        assertThat(json.path("user.address.street")).isEqualTo(dev.botta.json.Json.value("Avellaneda"))
        assertThat(json.path(".user.address.street")).isEqualTo(dev.botta.json.Json.value("Avellaneda"))
    }

    @Test
    fun `path returns null on invalid path`() {
        val json = JsonObject("user" to JsonObject("address" to JsonObject("street" to "Avellaneda")))
        assertThat(json.path("address.street")).isNull()
        assertThat(json.path(".address.street")).isNull()
    }

    @Test
    fun `path nested array and objects`() {
        val json = JsonObject("user" to JsonObject(
            "addresses" to JsonArray(
                JsonObject("street" to "Avellaneda"),
                JsonObject("street" to "Cabildo"),
            )
        ))
        assertThat(json.path("user.addresses[1].street")).isEqualTo(dev.botta.json.Json.value("Cabildo"))
        assertThat(json.path(".user.addresses[1].street")).isEqualTo(dev.botta.json.Json.value("Cabildo"))
    }

    @Test
    fun `path fails with invalid key`() {
        assertThrows<IllegalArgumentException> { JsonObject().path("..") }
    }

    private val obj = JsonObject()
    private val writer = mockk<MinimalJsonWriter>(relaxed = true)
}
