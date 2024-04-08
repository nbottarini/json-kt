package dev.botta.json.extensions

import dev.botta.json.values.JsonArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ListExtensionsTest {
    @Test
    fun toJson() {
        assertThat(listOf(1, 2, 3, 4).toJson()).isEqualTo(JsonArray(1, 2, 3, 4))
        assertThat(listOf(1L, 2L, 3L, 4L).toJson()).isEqualTo(JsonArray(1L, 2L, 3L, 4L))
        assertThat(listOf(3.14f, 2.17f).toJson()).isEqualTo(JsonArray(3.14f, 2.17f))
        assertThat(listOf(3.14, 2.17).toJson()).isEqualTo(JsonArray(3.14, 2.17))
        assertThat(listOf("foo", "bar", null).toJson()).isEqualTo(JsonArray("foo", "bar", null))
        assertThat(listOf(true, false, true).toJson()).isEqualTo(JsonArray(true, false, true))
        assertThat(listOf(true, 3.14, "foo").toJson()).isEqualTo(JsonArray(true, 3.14, "foo"))
        assertThat(listOf(dev.botta.json.Json.value("foo"), dev.botta.json.Json.value(23)).toJson()).isEqualTo(JsonArray(
            dev.botta.json.Json.value("foo"), dev.botta.json.Json.value(23)))
    }

    @Test
    fun `toJson nested`() {
        val list = listOf(
            mapOf("id" to 1, "productName" to "Sunglasses"),
            mapOf("id" to 2, "productName" to "Smartwatch"),
        )
        val expected = dev.botta.json.Json.array(
            dev.botta.json.Json.obj("id" to 1, "productName" to "Sunglasses"),
            dev.botta.json.Json.obj("id" to 2, "productName" to "Smartwatch"),
        )

        assertThat(list.toJson()).isEqualTo(expected)
    }
}
