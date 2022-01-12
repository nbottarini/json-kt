package com.nbottarini.asimov.json.extensions

import com.nbottarini.asimov.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MapExtensionsTest {
    @Test
    fun toJson() {
        assertThat(mapOf("name" to "Paul", "lastname" to "McCartney").toJson()).isEqualTo(Json.obj("name" to "Paul", "lastname" to "McCartney"))
        assertThat(mapOf("name" to "Paul", "age" to 23).toJson()).isEqualTo(Json.obj("name" to "Paul", "age" to 23))
        assertThat(mapOf("isSelected" to true, "city" to null).toJson()).isEqualTo(Json.obj("isSelected" to true, "city" to null))
    }
}
