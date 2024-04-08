@file:Suppress("ClassName")

package dev.botta.json.values

import dev.botta.json.TestUtil
import dev.botta.json.writer.MinimalJsonWriter
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

class JsonArrayTest {
    @Test
    fun `create with list of values`() {
        assertThat(JsonArray(listOf(1, true, "foo"))).containsExactly(dev.botta.json.Json.value(1), dev.botta.json.Json.value(true), dev.botta.json.Json.value("foo"))
    }

    @Test
    fun `create with values`() {
        assertThat(JsonArray(1, true, "foo")).containsExactly(dev.botta.json.Json.value(1), dev.botta.json.Json.value(true), dev.botta.json.Json.value("foo"))
    }

    @Test
    fun `create nested array`() {
        assertThat(JsonArray(JsonArray())).containsExactly(JsonArray())
    }

    @Test
    fun equals() {
        assertThat(JsonArray()).isEqualTo(JsonArray())
        assertThat(JsonArray("foo", "bar")).isEqualTo(JsonArray("foo", "bar"))
        assertThat(JsonArray("foo", "bar", "baz")).isNotEqualTo(JsonArray("foo", "bar"))
        assertThat(JsonArray("foo", "bar")).isNotEqualTo(JsonArray("bar", "foo"))
    }

    @Nested
    inner class add {
        @Test
        fun `add json value`() {
            array.add(dev.botta.json.Json.value(23))
            array.add(dev.botta.json.Json.value(42))
            assertThat(array).isEqualTo(JsonArray(23, 42))
        }

        @Test
        fun `add int`() {
            array.add(24)
            array.add(43)
            assertThat(array).isEqualTo(JsonArray(24, 43))
        }

        @Test
        fun `add long`() {
            array.add(25L)
            array.add(44L)
            assertThat(array).isEqualTo(JsonArray(25, 44))
        }

        @Test
        fun `add float`() {
            array.add(3.14f)
            array.add(7.3f)
            assertThat(array).isEqualTo(JsonArray(3.14f, 7.3f))
        }

        @Test
        fun `add double`() {
            array.add(3.15)
            array.add(7.4)
            assertThat(array).isEqualTo(JsonArray(3.15f, 7.4f))
        }

        @Test
        fun `add boolean`() {
            array.add(true)
            array.add(false)
            assertThat(array).isEqualTo(JsonArray(true, false))
        }

        @Test
        fun `add string`() {
            array.add("foo")
            array.add("bar")
            array.add(null as String?)
            assertThat(array).isEqualTo(JsonArray("foo", "bar", null))
        }

        @Test
        fun `add json null`() {
            array.add(dev.botta.json.Json.NULL)
            assertThat(array).isEqualTo(JsonArray(null))
        }

        @Test
        fun `add json array`() {
            array.add(dev.botta.json.Json.array(1, 2))
            assertThat(array).containsExactly(dev.botta.json.Json.array(1, 2))
        }

        @Test
        fun `add json object`() {
            array.add(dev.botta.json.Json.obj())
            assertThat(array).containsExactly(dev.botta.json.Json.obj())
        }

        @Test
        fun `add with index int`() {
            val array = JsonArray(1, 2, 3)
            array.add(1, 5)
            assertThat(array).isEqualTo(JsonArray(1, 5, 2, 3))
        }

        @Test
        fun `add with index long`() {
            val array = JsonArray(1, 2, 3)
            array.add(1, 5L)
            assertThat(array).isEqualTo(JsonArray(1, 5, 2, 3))
        }

        @Test
        fun `add with index float`() {
            val array = JsonArray(1, 2, 3)
            array.add(1, 3.14f)
            assertThat(array).isEqualTo(JsonArray(1, 3.14f, 2, 3))
        }

        @Test
        fun `add with index double`() {
            val array = JsonArray(1, 2, 3)
            array.add(1, 3.14)
            assertThat(array).isEqualTo(JsonArray(1, 3.14, 2, 3))
        }

        @Test
        fun `add with index boolean`() {
            val array = JsonArray(1, 2, 3)
            array.add(1, true)
            assertThat(array).isEqualTo(JsonArray(1, true, 2, 3))
        }

        @Test
        fun `add with index json value`() {
            val array = JsonArray(1, 2, 3)
            array.add(1, dev.botta.json.Json.value(23))
            assertThat(array).isEqualTo(JsonArray(1, dev.botta.json.Json.value(23), 2, 3))
        }

        @Test
        fun addAll() {
            array.with(1)
            array.addAll(listOf(dev.botta.json.Json.value(2), dev.botta.json.Json.value(3)))
            assertThat(array).isEqualTo(JsonArray(1, 2, 3))
        }

        @Test
        fun `addAll different types`() {
            array.with(1)
            array.addAll(listOf(true, "foo"))
            assertThat(array).isEqualTo(JsonArray(1, true, "foo"))
        }

        @Test
        fun `addAll with index`() {
            array.with(1, 4)
            array.addAll(1, listOf(dev.botta.json.Json.value(2), dev.botta.json.Json.value(3)))
            assertThat(array).isEqualTo(JsonArray(1, 2, 3, 4))
        }

        @Test
        fun `addAll different types with index`() {
            array.with(1, 4)
            array.addAll(1, listOf(true, "foo"))
            assertThat(array).isEqualTo(JsonArray(1, true, "foo", 4))
        }
    }

    @Nested
    inner class with {
        @Test
        fun `with json value`() {
            assertThat(JsonArray().with(dev.botta.json.Json.value(23)).with(dev.botta.json.Json.value(42))).containsExactly(
                dev.botta.json.Json.value(23), dev.botta.json.Json.value(42))
            assertThat(JsonArray().with(dev.botta.json.Json.value(23), dev.botta.json.Json.value(42))).containsExactly(
                dev.botta.json.Json.value(23), dev.botta.json.Json.value(42))
        }

        @Test
        fun `with int`() {
            assertThat(JsonArray().with(24).with(43)).containsExactly(dev.botta.json.Json.value(24), dev.botta.json.Json.value(43))
            assertThat(JsonArray().with(24, 43)).containsExactly(dev.botta.json.Json.value(24), dev.botta.json.Json.value(43))
        }

        @Test
        fun `with long`() {
            assertThat(JsonArray().with(25L).with(44L)).containsExactly(dev.botta.json.Json.value(25), dev.botta.json.Json.value(44))
            assertThat(JsonArray().with(25L, 44L)).containsExactly(dev.botta.json.Json.value(25), dev.botta.json.Json.value(44))
        }

        @Test
        fun `with float`() {
            assertThat(JsonArray().with(3.14f).with(7.3f)).containsExactly(dev.botta.json.Json.value(3.14f), dev.botta.json.Json.value(7.3f))
            assertThat(JsonArray().with(3.14f, 7.3f)).containsExactly(dev.botta.json.Json.value(3.14f), dev.botta.json.Json.value(7.3f))
        }

        @Test
        fun `with double`() {
            assertThat(JsonArray().with(3.15).with(7.4)).containsExactly(dev.botta.json.Json.value(3.15f), dev.botta.json.Json.value(7.4f))
            assertThat(JsonArray().with(3.15, 7.4)).containsExactly(dev.botta.json.Json.value(3.15f), dev.botta.json.Json.value(7.4f))
        }

        @Test
        fun `with boolean`() {
            assertThat(JsonArray().with(true).with(false)).containsExactly(dev.botta.json.Json.TRUE, dev.botta.json.Json.FALSE)
            assertThat(JsonArray().with(true, false)).containsExactly(dev.botta.json.Json.TRUE, dev.botta.json.Json.FALSE)
        }

        @Test
        fun `with string`() {
            assertThat(JsonArray().with("foo").with("bar").with(null as String?)).containsExactly(dev.botta.json.Json.value("foo"), dev.botta.json.Json.value("bar"), dev.botta.json.Json.NULL)
            assertThat(JsonArray().with("foo", "bar", null as String?)).containsExactly(dev.botta.json.Json.value("foo"), dev.botta.json.Json.value("bar"), dev.botta.json.Json.NULL)
        }

        @Test
        fun `with json null`() {
            assertThat(JsonArray().with(dev.botta.json.Json.NULL)).containsExactly(dev.botta.json.Json.NULL)
        }

        @Test
        fun `with json array`() {
            assertThat(JsonArray().with(dev.botta.json.Json.array(1, 2))).containsExactly(dev.botta.json.Json.array(1, 2))
        }

        @Test
        fun `with json object`() {
            assertThat(JsonArray().with(dev.botta.json.Json.obj())).containsExactly(dev.botta.json.Json.obj())
        }

        @Test
        fun `with multiple types`() {
            assertThat(JsonArray().with(1, "foo", true)).containsExactly(dev.botta.json.Json.value(1), dev.botta.json.Json.value("foo"), dev.botta.json.Json.TRUE)
        }
    }

    @Nested
    inner class set {
        @Test
        fun `set int`() {
            array.add("foo")
            array[0] = 23
            assertThat(array).isEqualTo(JsonArray(23))
        }

        @Test
        fun `set long`() {
            array.add("foo")
            array[0] = 23L
            assertThat(array).isEqualTo(JsonArray(23L))
        }

        @Test
        fun `set float`() {
            array.add("foo")
            array[0] = 3.14f
            assertThat(array).isEqualTo(JsonArray(3.14f))
        }

        @Test
        fun `set double`() {
            array.add("foo")
            array[0] = 3.14
            assertThat(array).isEqualTo(JsonArray(3.14))
        }

        @Test
        fun `set boolean`() {
            array.add("foo")
            array[0] = false
            assertThat(array).isEqualTo(JsonArray(false))
        }

        @Test
        fun `set string`() {
            array.add(23)
            array[0] = "foo"
            assertThat(array).isEqualTo(JsonArray("foo"))
        }

        @Test
        fun `set nullable string`() {
            array.add(23)
            array[0] = null as String?
            assertThat(array).isEqualTo(JsonArray(null))
        }

        @Test
        fun `set json value`() {
            array.add(23)
            array[0] = dev.botta.json.Json.NULL
            assertThat(array).isEqualTo(JsonArray(dev.botta.json.Json.NULL))
        }

        @Test
        fun `set fails with invalid index`() {
            array.add(23)
            assertThrows<IndexOutOfBoundsException> { array[1] = true }
        }
    }

    @Test
    fun size() {
        assertThat(JsonArray().size).isEqualTo(0)
        assertThat(JsonArray(23, 42).size).isEqualTo(2)
    }

    @Test
    fun isEmpty() {
        assertThat(JsonArray().isEmpty()).isTrue
        assertThat(JsonArray(1, 2).isEmpty()).isFalse
    }

    @Test
    fun `iterator is empty after creation`() {
        assertThat(array.iterator().hasNext()).isFalse
    }

    @Test
    fun `iterator fails if concurrent modification`() {
        val iterator = array.iterator()
        array.add(23)
        assertThrows<ConcurrentModificationException> { iterator.next() }
    }

    @Test
    fun get() {
        assertThat(JsonArray(23)[0]).isEqualTo(dev.botta.json.Json.value(23))
        assertThat(JsonArray(1, 2, 3)[1]).isEqualTo(dev.botta.json.Json.value(2))
        assertThat(JsonArray(1, 2, 3)[2]).isEqualTo(dev.botta.json.Json.value(3))
    }

    @Nested
    inner class remove {
        @Test
        fun `removeAt fails with invalid index`() {
            array.add(23)
            assertThrows<IndexOutOfBoundsException> { array.removeAt(1) }
        }

        @Test
        fun removeAt() {
            val array = JsonArray("a", "b", "c")
            array.removeAt(1)
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `remove json value`() {
            val array = JsonArray("a", "b", "c")
            array.remove(dev.botta.json.Json.value("b"))
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `remove int`() {
            val array = JsonArray("a", 2, "c")
            array.remove(2)
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `remove long`() {
            val array = JsonArray("a", 2, "c")
            array.remove(2L)
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `remove float`() {
            val array = JsonArray("a", 3.14, "c")
            array.remove(3.14f)
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `remove double`() {
            val array = JsonArray("a", 3.14, "c")
            array.remove(3.14)
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `remove string`() {
            val array = JsonArray("a", "b", "c")
            array.remove("b")
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `remove boolean`() {
            val array = JsonArray("a", true, "c")
            array.remove(true)
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun removeAll() {
            val array = JsonArray("a", true, "c")
            array.removeAll(listOf("a", "c"))
            assertThat(array).isEqualTo(JsonArray(true))
        }

        @Test
        fun `removeAll json values`() {
            val array = JsonArray("a", true, "c")
            array.removeAll(listOf(dev.botta.json.Json.value("a"), dev.botta.json.Json.value("c")))
            assertThat(array).isEqualTo(JsonArray(true))
        }

        @Test
        fun retainAll() {
            val array = JsonArray("a", true, "c")
            array.retainAll(listOf("a", "c"))
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }

        @Test
        fun `retainAll json values`() {
            val array = JsonArray("a", true, "c")
            array.retainAll(listOf(dev.botta.json.Json.value("a"), dev.botta.json.Json.value("c")))
            assertThat(array).isEqualTo(JsonArray("a", "c"))
        }
    }

    @Nested
    inner class contains {
        @Test
        fun `contains json value`() {
            array.with(1, 2, 3)
            assertThat(array.contains(dev.botta.json.Json.value(2))).isTrue
        }

        @Test
        fun `contains int`() {
            array.with(1, 2, 3)
            assertThat(array.contains(2)).isTrue
        }

        @Test
        fun `contains long`() {
            array.with(1, 2, 3)
            assertThat(array.contains(2L)).isTrue
        }

        @Test
        fun `contains float`() {
            array.with(1, 3.14, 3)
            assertThat(array.contains(3.14f)).isTrue
        }

        @Test
        fun `contains double`() {
            array.with(1, 3.14, 3)
            assertThat(array.contains(3.14)).isTrue
        }

        @Test
        fun `contains boolean`() {
            array.with(1, true, 3)
            assertThat(array.contains(true)).isTrue
        }

        @Test
        fun `contains all`() {
            array.with(1, true, 3)
            assertThat(array.containsAll(listOf(true, 3))).isTrue
        }

        @Test
        fun `contains all json values`() {
            array.with(1, true, 3)
            assertThat(array.containsAll(listOf(dev.botta.json.Json.value(true), dev.botta.json.Json.value(3)))).isTrue
        }

        @Test
        fun `contains string`() {
            array.with(1, "foo", 3)
            assertThat(array.contains("foo")).isTrue
        }

        @Test
        fun `contains null`() {
            array.with(1, null, 3)
            assertThat(array.contains(null)).isTrue
        }
    }

    @Test
    fun clear() {
        array.with(1, null, 3)
        array.clear()
        assertThat(array.isEmpty()).isTrue
    }

    @Nested
    inner class indexOf {
        @Test
        fun `indexOf json value`() {
            array.with(1, null, 3)
            assertThat(array.indexOf(dev.botta.json.Json.value(3))).isEqualTo(2)
        }

        @Test
        fun `indexOf int`() {
            array.with(1, null, 3)
            assertThat(array.indexOf(3)).isEqualTo(2)
        }

        @Test
        fun `indexOf long`() {
            array.with(1, null, 3)
            assertThat(array.indexOf(3L)).isEqualTo(2)
        }

        @Test
        fun `indexOf float`() {
            array.with(1, null, 3.14)
            assertThat(array.indexOf(3.14f)).isEqualTo(2)
        }

        @Test
        fun `indexOf double`() {
            array.with(1, null, 3.14)
            assertThat(array.indexOf(3.14)).isEqualTo(2)
        }

        @Test
        fun `indexOf boolean`() {
            array.with(1, null, true)
            assertThat(array.indexOf(true)).isEqualTo(2)
        }

        @Test
        fun `indexOf string`() {
            array.with(1, null, "foo")
            assertThat(array.indexOf("foo")).isEqualTo(2)
        }

        @Test
        fun `indexOf null`() {
            array.with(1, null, "foo")
            assertThat(array.indexOf(null)).isEqualTo(1)
        }
    }

    @Test
    fun `write empty`() {
        JsonArray().write(writer)

        verifySequence {
            writer.writeArrayOpen()
            writer.writeArrayClose()
        }
        confirmVerified(writer)
    }

    @Test
    fun `write values`() {
        JsonArray(23, "foo", false).write(writer)

        verifySequence {
            writer.writeArrayOpen()
            writer.writeNumber("23")
            writer.writeArraySeparator()
            writer.writeString("foo")
            writer.writeArraySeparator()
            writer.writeLiteral("false")
            writer.writeArrayClose()
        }
        confirmVerified(writer)
    }

    @Test
    fun `is serializable`() {
        val array = JsonArray(3.14, 23, "foo", JsonArray(false))
        assertThat(TestUtil.serializeAndDeserialize(array)).isEqualTo(array)
    }

    @Test
    fun `to string`() {
        assertThat(JsonArray().toString()).isEqualTo("[]")
        assertThat(JsonArray(1, 2, 3).toString()).isEqualTo("[1,2,3]")
        assertThat(JsonArray(1L, 2L, 3L).toString()).isEqualTo("[1,2,3]")
        assertThat(JsonArray(3.14f, 7.13f).toString()).isEqualTo("[3.14,7.13]")
        assertThat(JsonArray(3.14, 7.13).toString()).isEqualTo("[3.14,7.13]")
        assertThat(JsonArray(true, false).toString()).isEqualTo("[true,false]")
        assertThat(JsonArray("foo", "bar").toString()).isEqualTo("[\"foo\",\"bar\"]")
        assertThat(JsonArray("foo", null).toString()).isEqualTo("[\"foo\",null]")
        assertThat(JsonArray("foo", JsonObject("a" to "bar")).toString()).isEqualTo("[\"foo\",{\"a\":\"bar\"}]")
    }

    @Test
    fun `to string nested arrays`() {
        assertThat(JsonArray(JsonArray()).toString()).isEqualTo("[[]]")
        assertThat(JsonArray(JsonArray(1, 2)).toString()).isEqualTo("[[1,2]]")
        assertThat(JsonArray("foo", JsonArray("bar", 3)).toString()).isEqualTo("[\"foo\",[\"bar\",3]]")
        assertThat(JsonArray(JsonArray("bar", 1), JsonArray("foo", 2)).toString()).isEqualTo("[[\"bar\",1],[\"foo\",2]]")
    }

    @Nested
    inner class `type conversions` {
        @Test
        fun `type checking`() {
            assertThat(JsonArray().isArray).isTrue
            assertThat(JsonArray().isObject).isFalse
            assertThat(JsonArray().isNumber).isFalse
            assertThat(JsonArray().isString).isFalse
            assertThat(JsonArray().isBoolean).isFalse
            assertThat(JsonArray().isNull).isFalse
            assertThat(JsonArray().isTrue).isFalse
            assertThat(JsonArray().isFalse).isFalse
        }

        @Test
        fun asObject() {
            val default = dev.botta.json.Json.obj("name" to "John")
            assertThat(JsonArray().asObject()).isNull()
            assertThat(JsonArray().asObject(default)).isEqualTo(default)
        }

        @Test
        fun asArray() {
            val default = dev.botta.json.Json.array(1, 2, 3)
            assertThat(JsonArray().asArray()).isNotNull
            assertThat(JsonArray().asArray(default)).isNotEqualTo(default)
        }

        @Test
        fun asInt() {
            val default = 55
            assertThat(JsonArray().asInt()).isNull()
            assertThat(JsonArray().asInt(default)).isEqualTo(default)
        }

        @Test
        fun asLong() {
            val default = 55L
            assertThat(JsonArray().asLong()).isNull()
            assertThat(JsonArray().asLong(default)).isEqualTo(default)
        }

        @Test
        fun asFloat() {
            val default = 7.14f
            assertThat(JsonArray().asFloat()).isNull()
            assertThat(JsonArray().asFloat(default)).isEqualTo(default)
        }

        @Test
        fun asDouble() {
            val default = 7.14
            assertThat(JsonArray().asDouble()).isNull()
            assertThat(JsonArray().asDouble(default)).isEqualTo(default)
        }

        @Test
        fun asString() {
            val default = "bar"
            assertThat(JsonArray().asString()).isNull()
            assertThat(JsonArray().asString(default)).isEqualTo(default)
        }

        @Test
        fun asBoolean() {
            val default = false
            assertThat(JsonArray().asBoolean()).isNull()
            assertThat(JsonArray().asBoolean(default)).isEqualTo(default)
        }
    }

    @Test
    fun `path simple array`() {
        val json = JsonArray(1, 2, 3)
        assertThat(json.path("[1]")).isEqualTo(dev.botta.json.Json.value(2))
    }

    @Test
    fun `path nested array`() {
        val json = JsonArray(1, 2, JsonArray("foo", "bar"))
        assertThat(json.path("[2][1]")).isEqualTo(dev.botta.json.Json.value("bar"))
    }

    @Test
    fun `path nested array and objects`() {
        val json = JsonArray(
            JsonObject("user" to JsonObject(
                "addresses" to dev.botta.json.Json.NULL
            )),
            JsonObject("user" to JsonObject(
                "addresses" to JsonArray(
                    JsonObject("street" to "Avellaneda"),
                    JsonObject("street" to "Cabildo"),
                )
            ))
        )
        assertThat(json.path("[1].user.addresses[1].street")).isEqualTo(dev.botta.json.Json.value("Cabildo"))
    }

    @Test
    fun `path fails with invalid array syntax`() {
        assertThrows<IllegalArgumentException> { JsonArray().path("") }
        assertThrows<IllegalArgumentException> { JsonArray().path("[") }
        assertThrows<IllegalArgumentException> { JsonArray().path("[0") }
        assertThrows<IllegalArgumentException> { JsonArray().path("]") }
        assertThrows<IllegalArgumentException> { JsonArray().path(".") }
        assertThrows<IllegalArgumentException> { JsonArray().path("as") }
        assertThrows<IllegalArgumentException> { JsonArray().path("[a]") }
    }

    private val array = JsonArray()
    private val writer = mockk<MinimalJsonWriter>(relaxed = true)
}
