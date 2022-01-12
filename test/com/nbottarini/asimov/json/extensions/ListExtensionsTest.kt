package com.nbottarini.asimov.json.extensions

import com.nbottarini.asimov.json.Json
import com.nbottarini.asimov.json.values.JsonArray
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
        assertThat(listOf(Json.value("foo"), Json.value(23)).toJson()).isEqualTo(JsonArray(Json.value("foo"), Json.value(23)))
    }
}
