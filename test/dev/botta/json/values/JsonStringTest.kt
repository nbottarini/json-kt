@file:Suppress("ClassName")

package dev.botta.json.values

import dev.botta.json.TestUtil
import dev.botta.json.writer.MinimalJsonWriter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.io.StringWriter

class JsonStringTest {
    @Nested
    inner class `type conversions` {
        @Test
        fun `type checking`() {
            assertThat(JsonString("foo").isString).isTrue
            assertThat(JsonString("foo").isNull).isFalse
            assertThat(JsonString("foo").isTrue).isFalse
            assertThat(JsonString("true").isTrue).isFalse
            assertThat(JsonString("foo").isFalse).isFalse
            assertThat(JsonString("false").isFalse).isFalse
            assertThat(JsonString("foo").isBoolean).isFalse
            assertThat(JsonString("true").isBoolean).isFalse
            assertThat(JsonString("foo").isArray).isFalse
            assertThat(JsonString("foo").isObject).isFalse
            assertThat(JsonString("foo").isNumber).isFalse
            assertThat(JsonString("23").isNumber).isFalse
        }

        @Test
        fun asObject() {
            val default = dev.botta.json.Json.obj("name" to "John")
            assertThat(JsonString("foo").asObject()).isNull()
            assertThat(JsonString("foo").asObject(default)).isEqualTo(default)
        }

        @Test
        fun asArray() {
            val default = dev.botta.json.Json.array(1, 2, 3)
            assertThat(JsonString("foo").asArray()).isNull()
            assertThat(JsonString("foo").asArray(default)).isEqualTo(default)
        }

        @Test
        fun asInt() {
            val default = 55
            assertThat(JsonString("foo").asInt()).isNull()
            assertThat(JsonString("50").asInt()).isNull()
            assertThat(JsonString("foo").asInt(default)).isEqualTo(default)
        }

        @Test
        fun asLong() {
            val default = 55L
            assertThat(JsonString("foo").asLong()).isNull()
            assertThat(JsonString("50").asLong()).isNull()
            assertThat(JsonString("foo").asLong(default)).isEqualTo(default)
        }

        @Test
        fun asFloat() {
            val default = 7.14f
            assertThat(JsonString("foo").asFloat()).isNull()
            assertThat(JsonString("3.14").asFloat()).isNull()
            assertThat(JsonString("foo").asFloat(default)).isEqualTo(default)
        }

        @Test
        fun asDouble() {
            val default = 7.14
            assertThat(JsonString("foo").asDouble()).isNull()
            assertThat(JsonString("3.14").asDouble()).isNull()
            assertThat(JsonString("foo").asDouble(default)).isEqualTo(default)
        }

        @Test
        fun asString() {
            val default = "bar"
            assertThat(JsonString("foo").asString()).isEqualTo("foo")
            assertThat(JsonString("foo").asString(default)).isEqualTo("foo")
        }

        @Test
        fun asBoolean() {
            val default = false
            assertThat(JsonString("foo").asBoolean()).isNull()
            assertThat(JsonString("true").asBoolean()).isNull()
            assertThat(JsonString("foo").asBoolean(default)).isEqualTo(default)
        }
    }

    @Test
    fun write() {
        val writer = StringWriter()
        val jsonWriter = MinimalJsonWriter(writer)
        JsonString("foo").write(jsonWriter)

        assertThat(writer.toString()).isEqualTo("\"foo\"")
    }

    @Test
    fun `write escaped string`() {
        val writer = StringWriter()
        val jsonWriter = MinimalJsonWriter(writer)
        JsonString("foo\\bar").write(jsonWriter)

        assertThat(writer.toString()).isEqualTo("\"foo\\\\bar\"")
    }

    @Test
    fun equals() {
        assertThat(JsonString("foo")).isEqualTo(JsonString("foo"))
        assertThat(JsonString("foo")).isNotEqualTo(JsonString(""))
        assertThat(JsonString("foo")).isNotEqualTo(JsonString("bar"))
        assertThat(JsonString("foo")).isNotEqualTo(null)
    }

    @Test
    fun `is serializable`() {
        assertThat(TestUtil.serializeAndDeserialize(JsonString("foo"))).isEqualTo(JsonString("foo"))
    }

    @Test
    fun path() {
        assertThat(JsonString("foo").path("")).isEqualTo(dev.botta.json.Json.value("foo"))
    }

    @Test
    fun `invalid path returns null`() {
        assertThat(JsonString("foo").path("asd")).isNull()
    }
}
