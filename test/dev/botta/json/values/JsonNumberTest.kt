@file:Suppress("ClassName")

package dev.botta.json.values

import dev.botta.json.TestUtil
import dev.botta.json.writer.MinimalJsonWriter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.StringWriter

class JsonNumberTest {
    @Nested
    inner class `type checking` {
        @Test
        fun `type checking is null`() {
            assertThat(JsonNumber("23").isNull).isFalse
            assertThat(JsonNumber("-23").isNull).isFalse
            assertThat(JsonNumber("3.14").isNull).isFalse
            assertThat(JsonNumber("-3.14").isNull).isFalse
            assertThat(JsonNumber("1e50").isNull).isFalse
            assertThat(JsonNumber("-1e50").isNull).isFalse
        }

        @Test
        fun `type checking is true`() {
            assertThat(JsonNumber("23").isTrue).isFalse
            assertThat(JsonNumber("-23").isTrue).isFalse
            assertThat(JsonNumber("3.14").isTrue).isFalse
            assertThat(JsonNumber("-3.14").isTrue).isFalse
            assertThat(JsonNumber("1e50").isTrue).isFalse
            assertThat(JsonNumber("-1e50").isTrue).isFalse
        }

        @Test
        fun `type checking is false`() {
            assertThat(JsonNumber("23").isFalse).isFalse
            assertThat(JsonNumber("-23").isFalse).isFalse
            assertThat(JsonNumber("3.14").isFalse).isFalse
            assertThat(JsonNumber("-3.14").isFalse).isFalse
            assertThat(JsonNumber("1e50").isFalse).isFalse
            assertThat(JsonNumber("-1e50").isFalse).isFalse
        }

        @Test
        fun `type checking is boolean`() {
            assertThat(JsonNumber("23").isBoolean).isFalse
            assertThat(JsonNumber("-23").isBoolean).isFalse
            assertThat(JsonNumber("3.14").isBoolean).isFalse
            assertThat(JsonNumber("-3.14").isBoolean).isFalse
            assertThat(JsonNumber("1e50").isBoolean).isFalse
            assertThat(JsonNumber("-1e50").isBoolean).isFalse
        }

        @Test
        fun `type checking is array`() {
            assertThat(JsonNumber("23").isArray).isFalse
            assertThat(JsonNumber("-23").isArray).isFalse
            assertThat(JsonNumber("3.14").isArray).isFalse
            assertThat(JsonNumber("-3.14").isArray).isFalse
            assertThat(JsonNumber("1e50").isArray).isFalse
            assertThat(JsonNumber("-1e50").isArray).isFalse
        }

        @Test
        fun `type checking is object`() {
            assertThat(JsonNumber("23").isObject).isFalse
            assertThat(JsonNumber("-23").isObject).isFalse
            assertThat(JsonNumber("3.14").isObject).isFalse
            assertThat(JsonNumber("-3.14").isObject).isFalse
            assertThat(JsonNumber("1e50").isObject).isFalse
            assertThat(JsonNumber("-1e50").isObject).isFalse
        }

        @Test
        fun `type checking is string`() {
            assertThat(JsonNumber("23").isString).isFalse
            assertThat(JsonNumber("-23").isString).isFalse
            assertThat(JsonNumber("3.14").isString).isFalse
            assertThat(JsonNumber("-3.14").isString).isFalse
            assertThat(JsonNumber("1e50").isString).isFalse
            assertThat(JsonNumber("-1e50").isString).isFalse
        }

        @Test
        fun `type checking is number`() {
            assertThat(JsonNumber("23").isNumber).isTrue
            assertThat(JsonNumber("-23").isNumber).isTrue
            assertThat(JsonNumber("3.14").isNumber).isTrue
            assertThat(JsonNumber("-3.14").isNumber).isTrue
            assertThat(JsonNumber("1e50").isNumber).isTrue
            assertThat(JsonNumber("-1e50").isNumber).isTrue
        }
    }

    @Nested
    inner class `type conversions` {
        @Test
        fun asObject() {
            val default = dev.botta.json.Json.obj("name" to "John")
            assertThat(JsonNumber("23").asObject()).isNull()
            assertThat(JsonNumber("-23").asObject()).isNull()
            assertThat(JsonNumber("3.14").asObject()).isNull()
            assertThat(JsonNumber("-3.14").asObject()).isNull()
            assertThat(JsonNumber("23").asObject(default)).isEqualTo(default)
            assertThat(JsonNumber("-23").asObject(default)).isEqualTo(default)
            assertThat(JsonNumber("3.14").asObject(default)).isEqualTo(default)
            assertThat(JsonNumber("-3.14").asObject(default)).isEqualTo(default)
        }

        @Test
        fun asArray() {
            val default = dev.botta.json.Json.array(1, 2, 3)
            assertThat(JsonNumber("23").asArray()).isNull()
            assertThat(JsonNumber("-23").asArray()).isNull()
            assertThat(JsonNumber("3.14").asArray()).isNull()
            assertThat(JsonNumber("-3.14").asArray()).isNull()
            assertThat(JsonNumber("23").asArray(default)).isEqualTo(default)
            assertThat(JsonNumber("-23").asArray(default)).isEqualTo(default)
            assertThat(JsonNumber("3.14").asArray(default)).isEqualTo(default)
            assertThat(JsonNumber("-3.14").asArray(default)).isEqualTo(default)
        }

        @Test
        fun asInt() {
            val default = 55
            assertThat(JsonNumber("23").asInt()).isEqualTo(23)
            assertThat(JsonNumber("-23").asInt()).isEqualTo(-23)
            assertThat(JsonNumber("3.14").asInt()).isNull()
            assertThat(JsonNumber("-3.14").asInt()).isNull()
            assertThat(JsonNumber("23").asInt(default)).isEqualTo(23)
            assertThat(JsonNumber("-23").asInt(default)).isEqualTo(-23)
            assertThat(JsonNumber("3.14").asInt(default)).isEqualTo(default)
            assertThat(JsonNumber("-3.14").asInt(default)).isEqualTo(default)
        }

        @Test
        fun asLong() {
            val default = 55L
            assertThat(JsonNumber("23").asLong()).isEqualTo(23L)
            assertThat(JsonNumber("-23").asLong()).isEqualTo(-23L)
            assertThat(JsonNumber("3.14").asLong()).isNull()
            assertThat(JsonNumber("-3.14").asLong()).isNull()
            assertThat(JsonNumber("23").asLong(default)).isEqualTo(23L)
            assertThat(JsonNumber("-23").asLong(default)).isEqualTo(-23L)
            assertThat(JsonNumber("3.14").asLong(default)).isEqualTo(default)
            assertThat(JsonNumber("-3.14").asLong(default)).isEqualTo(default)
        }


        @Test
        fun asFloat() {
            val default = 7.14f
            assertThat(JsonNumber("23").asFloat()).isEqualTo(23f)
            assertThat(JsonNumber("-23").asFloat()).isEqualTo(-23f)
            assertThat(JsonNumber("3.14").asFloat()).isEqualTo(3.14f)
            assertThat(JsonNumber("-3.14").asFloat()).isEqualTo(-3.14f)
            assertThat(JsonNumber("23").asFloat(default)).isEqualTo(23f)
            assertThat(JsonNumber("-23").asFloat(default)).isEqualTo(-23f)
            assertThat(JsonNumber("3.14").asFloat(default)).isEqualTo(3.14f)
            assertThat(JsonNumber("-3.14").asFloat(default)).isEqualTo(-3.14f)
        }

        @Test
        fun `asFloat returns infinity for exceeding numbers`() {
            assertThat(JsonNumber("1e50").asFloat()).isEqualTo(Float.POSITIVE_INFINITY)
            assertThat(JsonNumber("-1e50").asFloat()).isEqualTo(Float.NEGATIVE_INFINITY)
        }

        @Test
        fun asDouble() {
            val default = 7.14
            assertThat(JsonNumber("23").asDouble()).isEqualTo(23.0)
            assertThat(JsonNumber("-23").asDouble()).isEqualTo(-23.0)
            assertThat(JsonNumber("3.14").asDouble()).isEqualTo(3.14)
            assertThat(JsonNumber("-3.14").asDouble()).isEqualTo(-3.14)
            assertThat(JsonNumber("23").asDouble(default)).isEqualTo(23.0)
            assertThat(JsonNumber("-23").asDouble(default)).isEqualTo(-23.0)
            assertThat(JsonNumber("3.14").asDouble(default)).isEqualTo(3.14)
            assertThat(JsonNumber("-3.14").asDouble(default)).isEqualTo(-3.14)
        }

        @Test
        fun `asDouble returns infinity for exceeding numbers`() {
            assertThat(JsonNumber("1e500").asDouble()).isEqualTo(Double.POSITIVE_INFINITY)
            assertThat(JsonNumber("-1e500").asDouble()).isEqualTo(Double.NEGATIVE_INFINITY)
        }

        @Test
        fun asString() {
            val default = "foo"
            assertThat(JsonNumber("23").asString()).isEqualTo("23")
            assertThat(JsonNumber("-23").asString()).isEqualTo("-23")
            assertThat(JsonNumber("3.14").asString()).isEqualTo("3.14")
            assertThat(JsonNumber("-3.14").asString()).isEqualTo("-3.14")
            assertThat(JsonNumber("23").asString(default)).isEqualTo("23")
            assertThat(JsonNumber("-23").asString(default)).isEqualTo("-23")
            assertThat(JsonNumber("3.14").asString(default)).isEqualTo("3.14")
            assertThat(JsonNumber("-3.14").asString(default)).isEqualTo("-3.14")
        }

        @Test
        fun asBoolean() {
            val default = true
            assertThat(JsonNumber("23").asBoolean()).isNull()
            assertThat(JsonNumber("-23").asBoolean()).isNull()
            assertThat(JsonNumber("3.14").asBoolean()).isNull()
            assertThat(JsonNumber("-3.14").asBoolean()).isNull()
            assertThat(JsonNumber("23").asBoolean(default)).isEqualTo(default)
            assertThat(JsonNumber("-23").asBoolean(default)).isEqualTo(default)
            assertThat(JsonNumber("3.14").asBoolean(default)).isEqualTo(default)
            assertThat(JsonNumber("-3.14").asBoolean(default)).isEqualTo(default)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "23, 23",
        "-23, -23",
        "3.14, 3.14",
        "-3.14, -3.14",
    )
    fun write(input: String, output: String) {
        val jsonWriter = MinimalJsonWriter(writer)
        JsonNumber(input).write(jsonWriter)

        assertThat(writer.toString()).isEqualTo(output)
    }

    @Test
    fun `to string`() {
        assertThat(JsonNumber("23").toString()).isEqualTo("23")
        assertThat(JsonNumber("-23").toString()).isEqualTo("-23")
        assertThat(JsonNumber("23.45").toString()).isEqualTo("23.45")
        assertThat(JsonNumber("-23.45").toString()).isEqualTo("-23.45")
        assertThat(JsonNumber("1e5").toString()).isEqualTo("1e5")
    }

    @Test
    fun equals() {
        assertThat(JsonNumber("23")).isEqualTo(JsonNumber("23"))
        assertThat(JsonNumber("-23")).isEqualTo(JsonNumber("-23"))
        assertThat(JsonNumber("3.14")).isEqualTo(JsonNumber("3.14"))
        assertThat(JsonNumber("-3.14")).isEqualTo(JsonNumber("-3.14"))
        assertThat(JsonNumber("23")).isNotEqualTo(JsonNumber("42"))
        assertThat(JsonNumber("1e+5")).isNotEqualTo(JsonNumber("1e5"))
        assertThat(JsonNumber("23")).isNotEqualTo(null)
    }

    @Test
    fun `is serializable`() {
        assertThat(TestUtil.serializeAndDeserialize(JsonNumber("23"))).isEqualTo(JsonNumber("23"))
    }

    @Test
    fun path() {
        assertThat(JsonNumber("23").path("")).isEqualTo(dev.botta.json.Json.value(23))
        assertThat(JsonNumber("3.14").path("")).isEqualTo(dev.botta.json.Json.value(3.14))
    }

    @Test
    fun `invalid path returns null`() {
        assertThat(JsonNumber("23").path("asd")).isNull()
        assertThat(JsonNumber("3.14").path("asd")).isNull()
    }

    private val writer = StringWriter()
}
