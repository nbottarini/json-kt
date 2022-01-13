package com.nbottarini.asimov.json.writer

import com.nbottarini.asimov.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PrettyPrintWriterTest {
    @Test
    fun `indentWithSpaces empty array`() {
        val output = Json.array().toString(PrettyPrint.indentWithSpaces(2))
        assertThat(output).isEqualTo("[\n  \n]")
    }

    @Test
    fun `indentWithSpaces array`() {
        val output = Json.array(23, 42).toString(PrettyPrint.indentWithSpaces(2))
        assertThat(output).isEqualTo("[\n  23,\n  42\n]")
    }

    @Test
    fun `indentWithSpaces nested array`() {
        val output = Json.array(23, Json.array(42)).toString(PrettyPrint.indentWithSpaces(2))
        assertThat(output).isEqualTo("[\n  23,\n  [\n    42\n  ]\n]")
    }

    @Test
    fun `indentWithSpaces empty object`() {
        val output = Json.obj().toString(PrettyPrint.indentWithSpaces(2))
        assertThat(output).isEqualTo("{\n  \n}")
    }

    @Test
    fun `indentWithSpaces object`() {
        val output = Json.obj("a" to 23, "b" to 42).toString(PrettyPrint.indentWithSpaces(2))
        assertThat(output).isEqualTo("{\n  \"a\": 23,\n  \"b\": 42\n}")
    }

    @Test
    fun `indentWithSpaces nested object`() {
        val output = Json.obj("a" to 23, "b" to Json.obj("c" to 42)).toString(PrettyPrint.indentWithSpaces(2))
        assertThat(output).isEqualTo("{\n  \"a\": 23,\n  \"b\": {\n    \"c\": 42\n  }\n}")
    }

    @Test
    fun `indentWithSpaces zero spaces`() {
        val output = Json.array().with(23, 42).toString(PrettyPrint.indentWithSpaces(0))
        assertThat(output).isEqualTo("[\n23,\n42\n]")
    }

    @Test
    fun `indentWithSpaces one space`() {
        val output = Json.array(23, 42).toString(PrettyPrint.indentWithSpaces(1))
        assertThat(output).isEqualTo("[\n 23,\n 42\n]")
    }

    @Test
    fun intentWithTabs() {
        val output = Json.array(23, 42).toString(PrettyPrint.indentWithTabs())
        assertThat(output).isEqualTo("[\n\t23,\n\t42\n]")
    }

    @Test
    fun `singleLine with nested array`() {
        val output = Json.array(23, Json.array(42)).toString(PrettyPrint.singleLine())
        assertThat(output).isEqualTo("[23, [42]]")
    }

    @Test
    fun `singleLine with nested object`() {
        val output = Json.obj("a" to 23, "b" to Json.obj("c" to 42)).toString(PrettyPrint.singleLine())
        assertThat(output).isEqualTo("{\"a\": 23, \"b\": {\"c\": 42}}")
    }
}
