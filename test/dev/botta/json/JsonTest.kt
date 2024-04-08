package dev.botta.json

import dev.botta.json.values.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.io.StringReader

class JsonTest {
    @Test
    fun `literal constants`() {
        assertThat(dev.botta.json.Json.NULL.isNull).isTrue
        assertThat(dev.botta.json.Json.value(null)).isEqualTo(dev.botta.json.Json.NULL)
        assertThat(dev.botta.json.Json.TRUE.isTrue).isTrue
        assertThat(dev.botta.json.Json.value(true)).isEqualTo(dev.botta.json.Json.TRUE)
        assertThat(dev.botta.json.Json.FALSE.isFalse).isTrue
        assertThat(dev.botta.json.Json.value(false)).isEqualTo(dev.botta.json.Json.FALSE)
    }

    @Test
    fun `value int`() {
        assertThat(dev.botta.json.Json.value(0).toString()).isEqualTo("0")
        assertThat(dev.botta.json.Json.value(23).toString()).isEqualTo("23")
        assertThat(dev.botta.json.Json.value(-1).toString()).isEqualTo("-1")
        assertThat(dev.botta.json.Json.value(Integer.MAX_VALUE).toString()).isEqualTo("2147483647")
        assertThat(dev.botta.json.Json.value(Integer.MIN_VALUE).toString()).isEqualTo("-2147483648")
    }

    @Test
    fun `value long`() {
        assertThat(dev.botta.json.Json.value(0L).toString()).isEqualTo("0")
        assertThat(dev.botta.json.Json.value(Long.MAX_VALUE).toString()).isEqualTo("9223372036854775807")
        assertThat(dev.botta.json.Json.value(Long.MIN_VALUE).toString()).isEqualTo("-9223372036854775808")
    }

    @Test
    fun `value float`() {
        assertThat(dev.botta.json.Json.value(23.5f).toString()).isEqualTo("23.5")
        assertThat(dev.botta.json.Json.value(-3.1416f).toString()).isEqualTo("-3.1416")
        assertThat(dev.botta.json.Json.value(0.00000123f).toString()).isEqualTo("1.23E-6")
        assertThat(dev.botta.json.Json.value(-12300000f).toString()).isEqualTo("-1.23E7")
    }

    @Test
    fun `value float cuts off point zero`() {
        assertThat(dev.botta.json.Json.value(0f).toString()).isEqualTo("0")
        assertThat(dev.botta.json.Json.value(-1f).toString()).isEqualTo("-1")
        assertThat(dev.botta.json.Json.value(10f).toString()).isEqualTo("10")
    }

    @Test
    fun `value float fails with infinity`() {
        assertThrows<IllegalArgumentException> { dev.botta.json.Json.value(Float.POSITIVE_INFINITY) }
    }

    @Test
    fun `value float fails with NaN`() {
        assertThrows<IllegalArgumentException> { dev.botta.json.Json.value(Float.NaN) }
    }

    @Test
    fun `value double`() {
        assertThat(dev.botta.json.Json.value(23.5).toString()).isEqualTo("23.5")
        assertThat(dev.botta.json.Json.value(-3.1416).toString()).isEqualTo("-3.1416")
        assertThat(dev.botta.json.Json.value(0.00000123).toString()).isEqualTo("1.23E-6")
        assertThat(dev.botta.json.Json.value(1.7976931348623157E308).toString()).isEqualTo("1.7976931348623157E308")
    }

    @Test
    fun `value double cuts off point zero`() {
        assertThat(dev.botta.json.Json.value(0.0).toString()).isEqualTo("0")
        assertThat(dev.botta.json.Json.value(-1.0).toString()).isEqualTo("-1")
        assertThat(dev.botta.json.Json.value(10.0).toString()).isEqualTo("10")
    }

    @Test
    fun `value double fails with infinity`() {
        assertThrows<IllegalArgumentException> { dev.botta.json.Json.value(Double.POSITIVE_INFINITY) }
    }

    @Test
    fun `value double fails with NaN`() {
        assertThrows<IllegalArgumentException> { dev.botta.json.Json.value(Double.NaN) }
    }

    @Test
    fun `value boolean`() {
        assertThat(dev.botta.json.Json.value(true)).isEqualTo(dev.botta.json.Json.TRUE)
        assertThat(dev.botta.json.Json.value(false)).isEqualTo(dev.botta.json.Json.FALSE)
    }

    @Test
    fun `value string`() {
        assertThat(dev.botta.json.Json.value("").asString()).isEqualTo("")
        assertThat(dev.botta.json.Json.value("Hello").asString()).isEqualTo("Hello")
        assertThat(dev.botta.json.Json.value("\"Hello\"").asString()).isEqualTo("\"Hello\"")
    }

    @Test
    fun `value nullable string`() {
        val value: String? = null
        assertThat(dev.botta.json.Json.value(value)).isEqualTo(dev.botta.json.Json.NULL)
    }

    @Test
    fun array() {
        assertThat(dev.botta.json.Json.array()).isEqualTo(JsonArray())
    }

    @Test
    fun `array ints`() {
        assertThat(dev.botta.json.Json.array(23, 42)).isEqualTo(JsonArray(23, 42))
    }

    @Test
    fun `array longs`() {
        assertThat(dev.botta.json.Json.array(23L, 42L)).isEqualTo(JsonArray(23L, 42L))
    }

    @Test
    fun `array floats`() {
        assertThat(dev.botta.json.Json.array(3.14f, 1.41f)).isEqualTo(JsonArray(3.14f, 1.41f))
    }

    @Test
    fun `array doubles`() {
        assertThat(dev.botta.json.Json.array(3.14, 1.41)).isEqualTo(JsonArray(3.14, 1.41))
    }

    @Test
    fun `array booleans`() {
        assertThat(dev.botta.json.Json.array(true, false)).isEqualTo(JsonArray(true, false))
    }

    @Test
    fun `array strings`() {
        assertThat(dev.botta.json.Json.array("foo", "bar")).isEqualTo(JsonArray("foo", "bar"))
    }

    @Test
    fun `object`() {
        assertThat(dev.botta.json.Json.obj()).isEqualTo(JsonObject())
        assertThat(dev.botta.json.Json.obj("name" to "Paul", "lastname" to "McCartney")).isEqualTo(JsonObject("name" to "Paul", "lastname" to "McCartney"))
        assertThat(dev.botta.json.Json.obj("name" to "Paul", "age" to 23)).isEqualTo(JsonObject("name" to "Paul", "age" to 23))
        assertThat(dev.botta.json.Json.obj("isSelected" to true, "city" to null)).isEqualTo(JsonObject("isSelected" to true, "city" to null))
    }

    @Test
    fun `parse string`() {
        assertThat(dev.botta.json.Json.parse("23")).isEqualTo(dev.botta.json.Json.value(23))
        assertThat(dev.botta.json.Json.parse("[23]")).isEqualTo(dev.botta.json.Json.array(dev.botta.json.Json.value(23)))
        assertThat(dev.botta.json.Json.parse("[23,{\"name\":\"Paul\"}]")).isEqualTo(dev.botta.json.Json.array(dev.botta.json.Json.value(23), dev.botta.json.Json.obj("name" to "Paul")))
    }

    @Test
    fun `parse reader`() {
        assertThat(dev.botta.json.Json.parse(StringReader("23"))).isEqualTo(dev.botta.json.Json.value(23))
        assertThat(dev.botta.json.Json.parse(StringReader("[23]"))).isEqualTo(dev.botta.json.Json.array(dev.botta.json.Json.value(23)))
        assertThat(dev.botta.json.Json.parse(StringReader("[23,{\"name\":\"Paul\"}]"))).isEqualTo(
            dev.botta.json.Json.array(
                dev.botta.json.Json.value(23), dev.botta.json.Json.obj("name" to "Paul")))
    }
}
