@file:Suppress("ClassName")

package dev.botta.json.values

import dev.botta.json.TestUtil
import dev.botta.json.writer.MinimalJsonWriter
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

class JsonLiteralTest {
    @Nested
    inner class `null` {
        @Test
        fun `type checking`() {
            assertThat(JsonLiteral.NULL.isNull).isTrue
            assertThat(JsonLiteral.NULL.isTrue).isFalse
            assertThat(JsonLiteral.NULL.isFalse).isFalse
            assertThat(JsonLiteral.NULL.isBoolean).isFalse
            assertThat(JsonLiteral.NULL.isArray).isFalse
            assertThat(JsonLiteral.NULL.isObject).isFalse
            assertThat(JsonLiteral.NULL.isString).isFalse
            assertThat(JsonLiteral.NULL.isNumber).isFalse
        }

        @Test
        fun asObject() {
            val default = dev.botta.json.Json.obj("name" to "John")
            assertThat(JsonLiteral.NULL.asObject()).isNull()
            assertThat(JsonLiteral.NULL.asObject(default)).isEqualTo(default)
        }

        @Test
        fun asArray() {
            val default = dev.botta.json.Json.array(1, 2, 3)
            assertThat(JsonLiteral.NULL.asArray()).isNull()
            assertThat(JsonLiteral.NULL.asArray(default)).isEqualTo(default)
        }

        @Test
        fun asInt() {
            val default = 55
            assertThat(JsonLiteral.NULL.asInt()).isNull()
            assertThat(JsonLiteral.NULL.asInt(default)).isEqualTo(default)
        }

        @Test
        fun asLong() {
            val default = 55L
            assertThat(JsonLiteral.NULL.asLong()).isNull()
            assertThat(JsonLiteral.NULL.asLong(default)).isEqualTo(default)
        }

        @Test
        fun asFloat() {
            val default = 7.14f
            assertThat(JsonLiteral.NULL.asFloat()).isNull()
            assertThat(JsonLiteral.NULL.asFloat(default)).isEqualTo(default)
        }

        @Test
        fun asDouble() {
            val default = 7.14
            assertThat(JsonLiteral.NULL.asDouble()).isNull()
            assertThat(JsonLiteral.NULL.asDouble(default)).isEqualTo(default)
        }

        @Test
        fun asString() {
            val default = "bar"
            assertThat(JsonLiteral.NULL.asString()).isNull()
            assertThat(JsonLiteral.NULL.asString(default)).isEqualTo(default)
        }

        @Test
        fun asBoolean() {
            val default = false
            assertThat(JsonLiteral.NULL.asBoolean()).isNull()
            assertThat(JsonLiteral.NULL.asBoolean(default)).isEqualTo(default)
        }

        @Test
        fun `write null`() {
            JsonLiteral.NULL.write(writer)

            verify(exactly = 1) { writer.writeLiteral("null") }
            confirmVerified(writer)
        }

        @Test
        fun `to string`() {
            assertThat(JsonLiteral.NULL.toString()).isEqualTo("null")
        }

        @Test
        fun equals() {
            assertThat(JsonLiteral.NULL).isEqualTo(JsonLiteral.NULL)
            assertThat(JsonLiteral.NULL).isEqualTo(dev.botta.json.Json.value(null))
            assertThat(JsonLiteral.NULL).isNotEqualTo(null)
            assertThat(JsonLiteral.NULL).isNotEqualTo(JsonLiteral.TRUE)
            assertThat(JsonLiteral.NULL).isNotEqualTo(JsonLiteral.FALSE)
            assertThat(JsonLiteral.NULL).isNotEqualTo(dev.botta.json.Json.value("null"))
        }

        @Test
        fun `is serializable`() {
            assertThat(TestUtil.serializeAndDeserialize(JsonLiteral.NULL)).isEqualTo(JsonLiteral.NULL)
        }

        @Test
        fun path() {
            assertThat(JsonLiteral.NULL.path("")).isEqualTo(JsonLiteral.NULL)
        }

        @Test
        fun `invalid path returns null`() {
            assertThat(JsonLiteral.NULL.path("asd")).isNull()
        }
    }

    @Nested
    inner class `true` {
        @Test
        fun `type checking`() {
            assertThat(JsonLiteral.TRUE.isTrue).isTrue
            assertThat(JsonLiteral.TRUE.isBoolean).isTrue
            assertThat(JsonLiteral.TRUE.isNull).isFalse
            assertThat(JsonLiteral.TRUE.isFalse).isFalse
            assertThat(JsonLiteral.TRUE.isArray).isFalse
            assertThat(JsonLiteral.TRUE.isObject).isFalse
            assertThat(JsonLiteral.TRUE.isString).isFalse
            assertThat(JsonLiteral.TRUE.isNumber).isFalse
        }

        @Test
        fun asObject() {
            val default = dev.botta.json.Json.obj("name" to "John")
            assertThat(JsonLiteral.TRUE.asObject()).isNull()
            assertThat(JsonLiteral.TRUE.asObject(default)).isEqualTo(default)
        }

        @Test
        fun asArray() {
            val default = dev.botta.json.Json.array(1, 2, 3)
            assertThat(JsonLiteral.TRUE.asArray()).isNull()
            assertThat(JsonLiteral.TRUE.asArray(default)).isEqualTo(default)
        }

        @Test
        fun asInt() {
            val default = 55
            assertThat(JsonLiteral.TRUE.asInt()).isNull()
            assertThat(JsonLiteral.TRUE.asInt(default)).isEqualTo(default)
        }

        @Test
        fun asLong() {
            val default = 55L
            assertThat(JsonLiteral.TRUE.asLong()).isNull()
            assertThat(JsonLiteral.TRUE.asLong(default)).isEqualTo(default)
        }

        @Test
        fun asFloat() {
            val default = 7.14f
            assertThat(JsonLiteral.TRUE.asFloat()).isNull()
            assertThat(JsonLiteral.TRUE.asFloat(default)).isEqualTo(default)
        }

        @Test
        fun asDouble() {
            val default = 7.14
            assertThat(JsonLiteral.TRUE.asDouble()).isNull()
            assertThat(JsonLiteral.TRUE.asDouble(default)).isEqualTo(default)
        }

        @Test
        fun asString() {
            val default = "bar"
            assertThat(JsonLiteral.TRUE.asString()).isNull()
            assertThat(JsonLiteral.TRUE.asString(default)).isEqualTo(default)
        }

        @Test
        fun asBoolean() {
            val default = false
            assertThat(JsonLiteral.TRUE.asBoolean()).isEqualTo(true)
            assertThat(JsonLiteral.TRUE.asBoolean(default)).isEqualTo(true)
        }

        @Test
        fun write() {
            JsonLiteral.TRUE.write(writer)

            verify(exactly = 1) { writer.writeLiteral("true") }
            confirmVerified(writer)
        }

        @Test
        fun equals() {
            assertThat(JsonLiteral.TRUE).isEqualTo(JsonLiteral.TRUE)
            assertThat(JsonLiteral.TRUE).isNotEqualTo(null)
            assertThat(JsonLiteral.TRUE).isNotEqualTo(JsonLiteral.FALSE)
            assertThat(JsonLiteral.TRUE).isNotEqualTo(true)
            assertThat(JsonLiteral.TRUE).isNotEqualTo(dev.botta.json.Json.value("true"))
        }

        @Test
        fun `is serializable`() {
            assertThat(TestUtil.serializeAndDeserialize(JsonLiteral.TRUE)).isEqualTo(JsonLiteral.TRUE)
        }

        @Test
        fun `to string`() {
            assertThat(JsonLiteral.TRUE.toString()).isEqualTo("true")
        }

        @Test
        fun path() {
            assertThat(JsonLiteral.TRUE.path("")).isEqualTo(JsonLiteral.TRUE)
        }

        @Test
        fun `invalid path returns null`() {
            assertThat(JsonLiteral.TRUE.path("asd")).isNull()
        }
    }

    @Nested
    inner class `false` {
        @Test
        fun `type checking`() {
            assertThat(JsonLiteral.FALSE.isFalse).isTrue
            assertThat(JsonLiteral.FALSE.isBoolean).isTrue
            assertThat(JsonLiteral.FALSE.isNull).isFalse
            assertThat(JsonLiteral.FALSE.isTrue).isFalse
            assertThat(JsonLiteral.FALSE.isArray).isFalse
            assertThat(JsonLiteral.FALSE.isObject).isFalse
            assertThat(JsonLiteral.FALSE.isString).isFalse
            assertThat(JsonLiteral.FALSE.isNumber).isFalse
        }

        @Test
        fun asObject() {
            val default = dev.botta.json.Json.obj("name" to "John")
            assertThat(JsonLiteral.FALSE.asObject()).isNull()
            assertThat(JsonLiteral.FALSE.asObject(default)).isEqualTo(default)
        }

        @Test
        fun asArray() {
            val default = dev.botta.json.Json.array(1, 2, 3)
            assertThat(JsonLiteral.FALSE.asArray()).isNull()
            assertThat(JsonLiteral.FALSE.asArray(default)).isEqualTo(default)
        }

        @Test
        fun asInt() {
            val default = 55
            assertThat(JsonLiteral.FALSE.asInt()).isNull()
            assertThat(JsonLiteral.FALSE.asInt(default)).isEqualTo(default)
        }

        @Test
        fun asLong() {
            val default = 55L
            assertThat(JsonLiteral.FALSE.asLong()).isNull()
            assertThat(JsonLiteral.FALSE.asLong(default)).isEqualTo(default)
        }

        @Test
        fun asFloat() {
            val default = 7.14f
            assertThat(JsonLiteral.FALSE.asFloat()).isNull()
            assertThat(JsonLiteral.FALSE.asFloat(default)).isEqualTo(default)
        }

        @Test
        fun asDouble() {
            val default = 7.14
            assertThat(JsonLiteral.FALSE.asDouble()).isNull()
            assertThat(JsonLiteral.FALSE.asDouble(default)).isEqualTo(default)
        }

        @Test
        fun asString() {
            val default = "bar"
            assertThat(JsonLiteral.FALSE.asString()).isNull()
            assertThat(JsonLiteral.FALSE.asString(default)).isEqualTo(default)
        }

        @Test
        fun asBoolean() {
            val default = true
            assertThat(JsonLiteral.FALSE.asBoolean()).isEqualTo(false)
            assertThat(JsonLiteral.FALSE.asBoolean(default)).isEqualTo(false)
        }

        @Test
        fun write() {
            JsonLiteral.FALSE.write(writer)

            verify(exactly = 1) { writer.writeLiteral("false") }
            confirmVerified(writer)
        }

        @Test
        fun `is serializable`() {
            assertThat(TestUtil.serializeAndDeserialize(JsonLiteral.FALSE)).isEqualTo(JsonLiteral.FALSE)
        }

        @Test
        fun equals() {
            assertThat(JsonLiteral.FALSE).isEqualTo(JsonLiteral.FALSE)
            assertThat(JsonLiteral.FALSE).isNotEqualTo(null)
            assertThat(JsonLiteral.FALSE).isNotEqualTo(JsonLiteral.TRUE)
            assertThat(JsonLiteral.FALSE).isNotEqualTo(false)
            assertThat(JsonLiteral.FALSE).isNotEqualTo(dev.botta.json.Json.value("false"))
        }

        @Test
        fun `to string`() {
            assertThat(JsonLiteral.FALSE.toString()).isEqualTo("false")
        }

        @Test
        fun path() {
            assertThat(JsonLiteral.FALSE.path("")).isEqualTo(JsonLiteral.FALSE)
        }

        @Test
        fun `invalid path returns null`() {
            assertThat(JsonLiteral.FALSE.path("asd")).isNull()
        }
    }

    private val writer = mockk<MinimalJsonWriter>(relaxed = true)
}
