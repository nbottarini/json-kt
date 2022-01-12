package com.nbottarini.asimov.json.writer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.StringWriter

class MinimalJsonWriterTest {
    @Test
    fun `write literal`() {
        writer.writeLiteral("foo")
        assertThat(output.toString()).isEqualTo("foo")
    }

    @Test
    fun `write number`() {
        writer.writeNumber("23")
        assertThat(output.toString()).isEqualTo("23")
    }

    @Test
    fun `write empty string`() {
        writer.writeString("")
        assertThat(output.toString()).isEqualTo("\"\"")
    }

    @Test
    fun `write string escapes backslashes`() {
        writer.writeString("foo\\bar")
        assertThat(output.toString()).isEqualTo("\"foo\\\\bar\"")
    }

    @Test
    fun `write array parts`() {
        writer.writeArrayOpen()
        writer.writeArraySeparator()
        writer.writeArrayClose()
        assertThat(output.toString()).isEqualTo("[,]")
    }

    @Test
    fun `write object parts`() {
        writer.writeObjectOpen()
        writer.writeMemberSeparator()
        writer.writeObjectSeparator()
        writer.writeObjectClose()
        assertThat(output.toString()).isEqualTo("{:,}")
    }

    @Test
    fun `write string escapes quotes`() {
        writer.writeString("a\"b")
        assertThat(output.toString()).isEqualTo("\"a\\\"b\"")
    }

    @Test
    fun `write string escapes escaped quotes`() {
        writer.writeString("a\\\"b")
        assertThat(output.toString()).isEqualTo("\"a\\\\\\\"b\"")
    }

    @Test
    fun `write string escapes newline`() {
        writer.writeString("foo\nbar")
        assertThat(output.toString()).isEqualTo("\"foo\\nbar\"")
    }

    @Test
    fun `write string escapes windows newline`() {
        writer.writeString("foo\r\nbar")
        assertThat(output.toString()).isEqualTo("\"foo\\r\\nbar\"")
    }

    @Test
    fun `write string escapes tags`() {
        writer.writeString("foo\tbar")
        assertThat(output.toString()).isEqualTo("\"foo\\tbar\"")
    }

    @Test
    fun `write string escapes special characters`() {
        writer.writeString("foo\u2028bar\u2029")
        assertThat(output.toString()).isEqualTo("\"foo\\u2028bar\\u2029\"")
    }

    @Test
    fun `write string escapes zero character`() {
        writer.writeString(String(charArrayOf('f', 'o', 'o', Char(0), 'b', 'a', 'r')))
        assertThat(output.toString()).isEqualTo("\"foo\\u0000bar\"")
    }

    @Test
    fun `write string escapes escape character`() {
        writer.writeString(String(charArrayOf('f', 'o', 'o', Char(27), 'b', 'a', 'r')))
        assertThat(output.toString()).isEqualTo("\"foo\\u001bbar\"")
    }

    @Test
    fun `write string escapes control characters`() {
        writer.writeString(String(charArrayOf(Char(1), Char(8), Char(15), Char(16), Char(31))))
        assertThat(output.toString()).isEqualTo("\"\\u0001\\u0008\\u000f\\u0010\\u001f\"")
    }

    private val output = StringWriter()
    private val writer = MinimalJsonWriter(output)
}
