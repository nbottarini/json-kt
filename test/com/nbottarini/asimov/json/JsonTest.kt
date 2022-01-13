package com.nbottarini.asimov.json

import com.nbottarini.asimov.json.values.JsonArray
import com.nbottarini.asimov.json.values.JsonObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.StringReader
import java.lang.IllegalArgumentException

class JsonTest {
    @Test
    fun `literal constants`() {
        assertThat(Json.NULL.isNull).isTrue
        assertThat(Json.value(null)).isEqualTo(Json.NULL)
        assertThat(Json.TRUE.isTrue).isTrue
        assertThat(Json.value(true)).isEqualTo(Json.TRUE)
        assertThat(Json.FALSE.isFalse).isTrue
        assertThat(Json.value(false)).isEqualTo(Json.FALSE)
    }

    @Test
    fun `value int`() {
        assertThat(Json.value(0).toString()).isEqualTo("0")
        assertThat(Json.value(23).toString()).isEqualTo("23")
        assertThat(Json.value(-1).toString()).isEqualTo("-1")
        assertThat(Json.value(Integer.MAX_VALUE).toString()).isEqualTo("2147483647")
        assertThat(Json.value(Integer.MIN_VALUE).toString()).isEqualTo("-2147483648")
    }

    @Test
    fun `value long`() {
        assertThat(Json.value(0L).toString()).isEqualTo("0")
        assertThat(Json.value(Long.MAX_VALUE).toString()).isEqualTo("9223372036854775807")
        assertThat(Json.value(Long.MIN_VALUE).toString()).isEqualTo("-9223372036854775808")
    }

    @Test
    fun `value float`() {
        assertThat(Json.value(23.5f).toString()).isEqualTo("23.5")
        assertThat(Json.value(-3.1416f).toString()).isEqualTo("-3.1416")
        assertThat(Json.value(0.00000123f).toString()).isEqualTo("1.23E-6")
        assertThat(Json.value(-12300000f).toString()).isEqualTo("-1.23E7")
    }

    @Test
    fun `value float cuts off point zero`() {
        assertThat(Json.value(0f).toString()).isEqualTo("0")
        assertThat(Json.value(-1f).toString()).isEqualTo("-1")
        assertThat(Json.value(10f).toString()).isEqualTo("10")
    }

    @Test
    fun `value float fails with infinity`() {
        assertThrows<IllegalArgumentException> { Json.value(Float.POSITIVE_INFINITY) }
    }

    @Test
    fun `value float fails with NaN`() {
        assertThrows<IllegalArgumentException> { Json.value(Float.NaN) }
    }

    @Test
    fun `value double`() {
        assertThat(Json.value(23.5).toString()).isEqualTo("23.5")
        assertThat(Json.value(-3.1416).toString()).isEqualTo("-3.1416")
        assertThat(Json.value(0.00000123).toString()).isEqualTo("1.23E-6")
        assertThat(Json.value(1.7976931348623157E308).toString()).isEqualTo("1.7976931348623157E308")
    }

    @Test
    fun `value double cuts off point zero`() {
        assertThat(Json.value(0.0).toString()).isEqualTo("0")
        assertThat(Json.value(-1.0).toString()).isEqualTo("-1")
        assertThat(Json.value(10.0).toString()).isEqualTo("10")
    }

    @Test
    fun `value double fails with infinity`() {
        assertThrows<IllegalArgumentException> { Json.value(Double.POSITIVE_INFINITY) }
    }

    @Test
    fun `value double fails with NaN`() {
        assertThrows<IllegalArgumentException> { Json.value(Double.NaN) }
    }

    @Test
    fun `value boolean`() {
        assertThat(Json.value(true)).isEqualTo(Json.TRUE)
        assertThat(Json.value(false)).isEqualTo(Json.FALSE)
    }

    @Test
    fun `value string`() {
        assertThat(Json.value("").asString()).isEqualTo("")
        assertThat(Json.value("Hello").asString()).isEqualTo("Hello")
        assertThat(Json.value("\"Hello\"").asString()).isEqualTo("\"Hello\"")
    }

    @Test
    fun `value nullable string`() {
        val value: String? = null
        assertThat(Json.value(value)).isEqualTo(Json.NULL)
    }

    @Test
    fun array() {
        assertThat(Json.array()).isEqualTo(JsonArray())
    }

    @Test
    fun `array ints`() {
        assertThat(Json.array(23, 42)).isEqualTo(JsonArray(23, 42))
    }

    @Test
    fun `array longs`() {
        assertThat(Json.array(23L, 42L)).isEqualTo(JsonArray(23L, 42L))
    }

    @Test
    fun `array floats`() {
        assertThat(Json.array(3.14f, 1.41f)).isEqualTo(JsonArray(3.14f, 1.41f))
    }

    @Test
    fun `array doubles`() {
        assertThat(Json.array(3.14, 1.41)).isEqualTo(JsonArray(3.14, 1.41))
    }

    @Test
    fun `array booleans`() {
        assertThat(Json.array(true, false)).isEqualTo(JsonArray(true, false))
    }

    @Test
    fun `array strings`() {
        assertThat(Json.array("foo", "bar")).isEqualTo(JsonArray("foo", "bar"))
    }

    @Test
    fun `object`() {
        assertThat(Json.obj()).isEqualTo(JsonObject())
        assertThat(Json.obj("name" to "Paul", "lastname" to "McCartney")).isEqualTo(JsonObject("name" to "Paul", "lastname" to "McCartney"))
        assertThat(Json.obj("name" to "Paul", "age" to 23)).isEqualTo(JsonObject("name" to "Paul", "age" to 23))
        assertThat(Json.obj("isSelected" to true, "city" to null)).isEqualTo(JsonObject("isSelected" to true, "city" to null))
    }

    @Test
    fun `parse string`() {
        assertThat(Json.parse("23")).isEqualTo(Json.value(23))
        assertThat(Json.parse("[23]")).isEqualTo(Json.array(Json.value(23)))
        assertThat(Json.parse("[23,{\"name\":\"Paul\"}]")).isEqualTo(Json.array(Json.value(23), Json.obj("name" to "Paul")))
    }

    @Test
    fun `parse reader`() {
        assertThat(Json.parse(StringReader("23"))).isEqualTo(Json.value(23))
        assertThat(Json.parse(StringReader("[23]"))).isEqualTo(Json.array(Json.value(23)))
        assertThat(Json.parse(StringReader("[23,{\"name\":\"Paul\"}]"))).isEqualTo(Json.array(Json.value(23), Json.obj("name" to "Paul")))
    }
}
