package dev.botta.json.parser

import dev.botta.json.values.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.io.StringReader

class JsonParserTest {
    @Test
    fun `fails with empty string`() {
        assertParseError(0, "Unexpected end of input") { parser.parse("") }
    }

    @Test
    fun `parse null`() {
        parser.parse("null")

        assertThat(loggedLines()).containsExactly("startNull 0", "endNull 4")
    }

    @Test
    fun `parse true`() {
        parser.parse("true")

        assertThat(loggedLines()).containsExactly("startBoolean 0", "endBoolean true 4")
    }

    @Test
    fun `parse false`() {
        parser.parse("false")

        assertThat(loggedLines()).containsExactly("startBoolean 0", "endBoolean false 5")
    }

    @Test
    fun `parse string`() {
        parser.parse("\"foo\"")

        assertThat(loggedLines()).containsExactly("startString 0", "endString foo 5")
    }

    @Test
    fun `parse empty string`() {
        parser.parse("\"\"")

        assertThat(loggedLines()).containsExactly("startString 0", "endString  2")
    }

    @Test
    fun `parse number`() {
        parser.parse("23")

        assertThat(loggedLines()).containsExactly("startNumber 0", "endNumber 23 2")
    }

    @Test
    fun `parse number negative`() {
        parser.parse("-23")

        assertThat(loggedLines()).containsExactly("startNumber 0", "endNumber -23 3")
    }

    @Test
    fun `parse number negative exponent`() {
        parser.parse("-2.3e-12")

        assertThat(loggedLines()).containsExactly("startNumber 0", "endNumber -2.3e-12 8")
    }

    @Test
    fun `parse array`() {
        parser.parse("[23]")

        assertThat(loggedLines()).containsExactly(
            "startArray 0",
            "startArrayValue [] 1",
            "startNumber 1",
            "endNumber 23 3",
            "endArrayValue [] 3",
            "endArray [] 4",
        )
    }

    @Test
    fun `parse array empty`() {
        parser.parse("[]")

        assertThat(loggedLines()).containsExactly(
            "startArray 0",
            "endArray [] 2",
        )
    }

    @Test
    fun `parse object`() {
        parser.parse("{\"foo\": 23}")

        assertThat(loggedLines()).containsExactly(
            "startObject 0",
            "startObjectName {} 1",
            "endObjectName {} foo 6",
            "startObjectValue {} foo 8",
            "startNumber 8",
            "endNumber 23 10",
            "endObjectValue {} foo 10",
            "endObject {} 11",
        )
    }

    @Test
    fun `parse object empty`() {
        parser.parse("{}")

        assertThat(loggedLines()).containsExactly(
            "startObject 0",
            "endObject {} 2",
        )
    }

    @Test
    fun `parse strips padding`() {
        assertThat(dev.botta.json.Json.parse(" [ ] ")).isEqualTo(JsonArray())
    }

    @Test
    fun `parse ignores all whitespace`() {
        assertThat(dev.botta.json.Json.parse("\t\r\n [\t\r\n ]\t\r\n ")).isEqualTo(JsonArray())
    }

    @Test
    fun `parse fails with unterminated string`() {
        assertParseError(5, "Unexpected end of input") { parser.parse("[\"foo") }
    }

    @Test
    fun `parse line and column on first line`() {
        parser.parse("[]")
        assertThat(handler.location.toString()).isEqualTo("1:3")
    }

    @Test
    fun `parse line and column after LF`() {
        parser.parse("[\n]")
        assertThat(handler.location.toString()).isEqualTo("2:2")
    }

    @Test
    fun `parse line and column after CRLF`() {
        parser.parse("[\r\n]")
        assertThat(handler.location.toString()).isEqualTo("2:2")
    }

    @Test
    fun `parse line and column after CR`() {
        parser.parse("[\r]")
        assertThat(handler.location.toString()).isEqualTo("1:4")
    }

    @Test
    fun `parse handles inputs that exceeds buffer size`() {
        val handler = DefaultJsonParserHandler()
        val parser = JsonParser(handler)
        val input = "[ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47 ]"

        parser.parse(StringReader(input), buffersize = 3)

        assertThat(handler.value.toString()).isEqualTo("[2,3,5,7,11,13,17,19,23,29,31,37,41,43,47]")
    }

    @Test
    fun `parse handles strings that exceeds buffer size`() {
        val handler = DefaultJsonParserHandler()
        val parser = JsonParser(handler)
        val input = "[ \"lorem ipsum dolor sit amet\" ]"

        parser.parse(StringReader(input), buffersize = 3)

        assertThat(handler.value.toString()).isEqualTo("[\"lorem ipsum dolor sit amet\"]")
    }

    @Test
    fun `parse handles numbers that exceeds buffer size`() {
        val handler = DefaultJsonParserHandler()
        val parser = JsonParser(handler)
        val input = "[ 3.141592653589 ]"

        parser.parse(StringReader(input), buffersize = 3)

        assertThat(handler.value.toString()).isEqualTo("[3.141592653589]")
    }

    @Test
    fun `parse handles position correctly when input exceeds buffer size`() {
        val handler = DefaultJsonParserHandler()
        val parser = JsonParser(handler)
        val input = "{\n  \"a\": 23,\n  \"b\": 42,\n}"

        val e = assertThrows<JsonParseError> { parser.parse(StringReader(input), buffersize = 3) }

        assertThat(e.location).isEqualTo(Location(24, 4, 1))
    }

    @Test
    fun `parse fails on too deeply nested array`() {
        var array = JsonArray()
        repeat(1000) { array = JsonArray().with(array) }
        val input = array.toString()

        val e = assertThrows<JsonParseError> { parser.parse(input) }

        assertThat(e.message).isEqualTo("Nesting too deep at 1:1002")
    }

    @Test
    fun `parse fails on too deeply nested object`() {
        var obj = JsonObject()
        repeat(1000) { obj = JsonObject().with("foo", obj) }
        val input = obj.toString()

        val e = assertThrows<JsonParseError> { parser.parse(input) }

        assertThat(e.message).isEqualTo("Nesting too deep at 1:7002")
    }

    @Test
    fun `parse fails on too deeply nested mixed object`() {
        var value: JsonValue = JsonObject()
        repeat(1000) {
            if (it % 2 == 0) {
                value = JsonArray().with(value)
            } else {
                value = JsonObject().with("foo", value)
            }
        }
        val input = value.toString()

        val e = assertThrows<JsonParseError> { parser.parse(input) }

        assertThat(e.message).isEqualTo("Nesting too deep at 1:4002")
    }

    @Test
    fun `parse doesnt fail with many arrays`() {
        val array = JsonArray()
        repeat(1000) { array.add(JsonArray().add(7)) }
        val input = array.toString()

        assertDoesNotThrow { parser.parse(input) }
    }

    @Test
    fun `parse doesnt fail with many objects`() {
        val array = JsonArray()
        repeat(1000) { array.add(JsonObject().with("foo", 7)) }
        val input = array.toString()

        assertDoesNotThrow { parser.parse(input) }
    }

    @Test
    fun `array empty`() {
        assertThat(dev.botta.json.Json.parse("[]").toString()).isEqualTo("[]")
    }

    @Test
    fun `array single value`() {
        assertThat(dev.botta.json.Json.parse("[23]").toString()).isEqualTo("[23]")
    }

    @Test
    fun `array multiple values`() {
        assertThat(dev.botta.json.Json.parse("[23,42]").toString()).isEqualTo("[23,42]")
    }

    @Test
    fun `array whitespaces`() {
        assertThat(dev.botta.json.Json.parse("[ 23 , 42 ]").toString()).isEqualTo("[23,42]")
    }

    @Test
    fun `array nested`() {
        assertThat(dev.botta.json.Json.parse("[[23]]").toString()).isEqualTo("[[23]]")
        assertThat(dev.botta.json.Json.parse("[[[]]]").toString()).isEqualTo("[[[]]]")
        assertThat(dev.botta.json.Json.parse("[[23],42]").toString()).isEqualTo("[[23],42]")
        assertThat(dev.botta.json.Json.parse("[[23],[42]]").toString()).isEqualTo("[[23],[42]]")
        assertThat(dev.botta.json.Json.parse("[{\"foo\":[23]},{\"bar\":[42]}]").toString()).isEqualTo("[{\"foo\":[23]},{\"bar\":[42]}]")
    }

    @Test
    fun `array illegal syntax`() {
        assertParseError(1, "Expected value") { parser.parse("[,]") }
        assertParseError(4, "Expected ',' or ']'") { parser.parse("[23 42]") }
        assertParseError(4, "Expected value") { parser.parse("[23,]") }
    }

    @Test
    fun `array incomplete`() {
        assertParseError(1, "Unexpected end of input") { parser.parse("[") }
        assertParseError(2, "Unexpected end of input") { parser.parse("[ ") }
        assertParseError(3, "Unexpected end of input") { parser.parse("[23") }
        assertParseError(4, "Unexpected end of input") { parser.parse("[23 ") }
        assertParseError(4, "Unexpected end of input") { parser.parse("[23,") }
        assertParseError(5, "Unexpected end of input") { parser.parse("[23, ") }
    }

    @Test
    fun `object empty`() {
        assertThat(dev.botta.json.Json.parse("{}").toString()).isEqualTo("{}")
    }

    @Test
    fun `object single value`() {
        assertThat(dev.botta.json.Json.parse("{\"foo\":23}").toString()).isEqualTo("{\"foo\":23}")
    }

    @Test
    fun `object multiple values`() {
        assertThat(dev.botta.json.Json.parse("{\"foo\":23,\"bar\":42}").toString()).isEqualTo("{\"foo\":23,\"bar\":42}")
    }

    @Test
    fun `object whitespace`() {
        assertThat(dev.botta.json.Json.parse("{ \"foo\" : 23, \"bar\" : 42 }").toString()).isEqualTo("{\"foo\":23,\"bar\":42}")
    }

    @Test
    fun `object nested`() {
        assertThat(dev.botta.json.Json.parse("{\"foo\":{}}").toString()).isEqualTo("{\"foo\":{}}")
        assertThat(dev.botta.json.Json.parse("{\"foo\":{\"bar\": 42}}").toString()).isEqualTo("{\"foo\":{\"bar\":42}}")
        assertThat(dev.botta.json.Json.parse("{\"foo\":{\"bar\": {\"baz\": 42}}}").toString()).isEqualTo("{\"foo\":{\"bar\":{\"baz\":42}}}")
        assertThat(dev.botta.json.Json.parse("{\"foo\":[{\"bar\": {\"baz\": [[42]]}}]}").toString()).isEqualTo("{\"foo\":[{\"bar\":{\"baz\":[[42]]}}]}")
    }

    @Test
    fun `object illegal syntax`() {
        assertParseError(1, "Expected name") { parser.parse("{,}") }
        assertParseError(1, "Expected name") { parser.parse("{:}") }
        assertParseError(1, "Expected name") { parser.parse("{23}") }
        assertParseError(4, "Expected ':'") { parser.parse("{\"a\"}") }
        assertParseError(5, "Expected ':'") { parser.parse("{\"a\" \"b\"}") }
        assertParseError(5, "Expected value") { parser.parse("{\"a\":}") }
        assertParseError(8, "Expected name") { parser.parse("{\"a\":23,}") }
        assertParseError(8, "Expected name") { parser.parse("{\"a\":23,42") }
    }

    @Test
    fun `object incomplete`() {
        assertParseError(1, "Unexpected end of input") { parser.parse("{") }
        assertParseError(2, "Unexpected end of input") { parser.parse("{ ") }
        assertParseError(2, "Unexpected end of input") { parser.parse("{\"") }
        assertParseError(4, "Unexpected end of input") { parser.parse("{\"a\"") }
        assertParseError(5, "Unexpected end of input") { parser.parse("{\"a\" ") }
        assertParseError(5, "Unexpected end of input") { parser.parse("{\"a\":") }
        assertParseError(6, "Unexpected end of input") { parser.parse("{\"a\": ") }
        assertParseError(7, "Unexpected end of input") { parser.parse("{\"a\":23") }
        assertParseError(8, "Unexpected end of input") { parser.parse("{\"a\":23 ") }
        assertParseError(8, "Unexpected end of input") { parser.parse("{\"a\":23,") }
        assertParseError(9, "Unexpected end of input") { parser.parse("{\"a\":23, ") }
    }

    @Test
    fun `string empty`() {
        assertThat(dev.botta.json.Json.parse("\"\"").asString()).isEqualTo("")
    }

    @Test
    fun `string ascii`() {
        assertThat(dev.botta.json.Json.parse("\" \"").asString()).isEqualTo(" ")
        assertThat(dev.botta.json.Json.parse("\"a\"").asString()).isEqualTo("a")
        assertThat(dev.botta.json.Json.parse("\"foo\"").asString()).isEqualTo("foo")
        assertThat(dev.botta.json.Json.parse("\"A2-D2\"").asString()).isEqualTo("A2-D2")
        assertThat(dev.botta.json.Json.parse("\"\u007f\"").asString()).isEqualTo("\u007f")
    }

    @Test
    fun `string non ascii`() {
        assertThat(dev.botta.json.Json.parse("\"Русский\"").asString()).isEqualTo("Русский")
        assertThat(dev.botta.json.Json.parse("\"العربية\"").asString()).isEqualTo("العربية")
        assertThat(dev.botta.json.Json.parse("\"日本語\"").asString()).isEqualTo("日本語")
    }

    @Test
    fun `string fail with control characters`() {
        assertParseError(3, "Expected valid string character") { parser.parse("\"--\n--\"") }
        assertParseError(3, "Expected valid string character") { parser.parse("\"--\r\n--\"") }
        assertParseError(3, "Expected valid string character") { parser.parse("\"--\t--\"") }
        assertParseError(3, "Expected valid string character") { parser.parse("\"--\u0000--\"") }
        assertParseError(3, "Expected valid string character") { parser.parse("\"--\u001f--\"") }
    }

    @Test
    fun `string valid escapes`() {
        assertThat(dev.botta.json.Json.parse("\" \\\" \"").asString()).isEqualTo(" \" ")
        assertThat(dev.botta.json.Json.parse("\" \\\\ \"").asString()).isEqualTo(" \\ ")
        assertThat(dev.botta.json.Json.parse("\" \\/ \"").asString()).isEqualTo(" / ")
        assertThat(dev.botta.json.Json.parse("\" \\b \"").asString()).isEqualTo(" \u0008 ")
//        assertThat(Json.parse("\" \\f \"")?.asString()).isEqualTo(" \u000c ")
        assertThat(dev.botta.json.Json.parse("\" \\r \"").asString()).isEqualTo(" \r ")
        assertThat(dev.botta.json.Json.parse("\" \\n \"").asString()).isEqualTo(" \n ")
        assertThat(dev.botta.json.Json.parse("\" \\t \"").asString()).isEqualTo(" \t ")
    }

    @Test
    fun `string fail with invalid escapes`() {
        assertParseError(2, "Expected valid escape sequence") { parser.parse("\"\\a\"") }
        assertParseError(2, "Expected valid escape sequence") { parser.parse("\"\\x\"") }
        assertParseError(2, "Expected valid escape sequence") { parser.parse("\"\\000\"") }
    }

    @Test
    fun `string valid unicodes`() {
        assertThat(dev.botta.json.Json.parse("\"\\u0021\"").asString()).isEqualTo("\u0021")
        assertThat(dev.botta.json.Json.parse("\"\\u4711\"").asString()).isEqualTo("\u4711")
        assertThat(dev.botta.json.Json.parse("\"\\uffff\"").asString()).isEqualTo("\uffff")
        assertThat(dev.botta.json.Json.parse("\"\\uabcdx\"").asString()).isEqualTo("\uabcdx")
    }

    @Test
    fun `string fail with illegal unicode escapes`() {
        assertParseError(3, "Expected hexadecimal digit") { parser.parse("\"\\u \"") }
        assertParseError(3, "Expected hexadecimal digit") { parser.parse("\"\\ux\"") }
        assertParseError(5, "Expected hexadecimal digit") { parser.parse("\"\\u20 \"") }
        assertParseError(6, "Expected hexadecimal digit") { parser.parse("\"\\u000x\"") }
    }

    @Test
    fun `string incomplete`() {
        assertParseError(1, "Unexpected end of input") { parser.parse("\"") }
        assertParseError(4, "Unexpected end of input") { parser.parse("\"foo") }
        assertParseError(5, "Unexpected end of input") { parser.parse("\"foo\\") }
        assertParseError(6, "Unexpected end of input") { parser.parse("\"foo\\n") }
        assertParseError(6, "Unexpected end of input") { parser.parse("\"foo\\u") }
        assertParseError(7, "Unexpected end of input") { parser.parse("\"foo\\u0") }
        assertParseError(9, "Unexpected end of input") { parser.parse("\"foo\\u000") }
        assertParseError(10, "Unexpected end of input") { parser.parse("\"foo\\u0000") }
    }

    @Test
    fun `numbers int`() {
        assertThat(dev.botta.json.Json.parse("0")).isEqualTo(JsonNumber("0"))
        assertThat(dev.botta.json.Json.parse("-0")).isEqualTo(JsonNumber("-0"))
        assertThat(dev.botta.json.Json.parse("1")).isEqualTo(JsonNumber("1"))
        assertThat(dev.botta.json.Json.parse("-1")).isEqualTo(JsonNumber("-1"))
        assertThat(dev.botta.json.Json.parse("23")).isEqualTo(JsonNumber("23"))
        assertThat(dev.botta.json.Json.parse("-23")).isEqualTo(JsonNumber("-23"))
        assertThat(dev.botta.json.Json.parse("1234567890")).isEqualTo(JsonNumber("1234567890"))
        assertThat(dev.botta.json.Json.parse("123456789012345678901234567890")).isEqualTo(JsonNumber("123456789012345678901234567890"))
    }

    @Test
    fun `numbers minus zero`() {
        val value = dev.botta.json.Json.parse("-0")

        assertThat(value.asInt()).isEqualTo(0)
        assertThat(value.asLong()).isEqualTo(0L)
        assertThat(value.asFloat()).isEqualTo(0f)
        assertThat(value.asDouble()).isEqualTo(0.0)
    }

    @Test
    fun `numbers decimal`() {
        assertThat(dev.botta.json.Json.parse("0.23")).isEqualTo(JsonNumber("0.23"))
        assertThat(dev.botta.json.Json.parse("-0.23")).isEqualTo(JsonNumber("-0.23"))
        assertThat(dev.botta.json.Json.parse("1234567890.12345678901234567890")).isEqualTo(JsonNumber("1234567890.12345678901234567890"))
    }

    @Test
    fun `numbers with exponent`() {
        assertThat(dev.botta.json.Json.parse("0.1e9")).isEqualTo(JsonNumber("0.1e9"))
        assertThat(dev.botta.json.Json.parse("0.1E9")).isEqualTo(JsonNumber("0.1E9"))
        assertThat(dev.botta.json.Json.parse("-0.23e9")).isEqualTo(JsonNumber("-0.23e9"))
        assertThat(dev.botta.json.Json.parse("0.23e9")).isEqualTo(JsonNumber("0.23e9"))
        assertThat(dev.botta.json.Json.parse("0.23e+9")).isEqualTo(JsonNumber("0.23e+9"))
        assertThat(dev.botta.json.Json.parse("0.23e-9")).isEqualTo(JsonNumber("0.23e-9"))
    }

    @Test
    fun `numbers invalid format`() {
        assertParseError(0, "Expected value") { parser.parse("+1") }
        assertParseError(0, "Expected value") { parser.parse(".1") }
        assertParseError(1, "Unexpected character") { parser.parse("02") }
        assertParseError(2, "Unexpected character") { parser.parse("-02") }
        assertParseError(1, "Expected digit") { parser.parse("-x") }
        assertParseError(2, "Expected digit") { parser.parse("1.x") }
        assertParseError(2, "Expected digit") { parser.parse("1ex") }
        assertParseError(3, "Unexpected character") { parser.parse("1e1x") }
    }

    @Test
    fun `numbers incomplete`() {
        assertParseError(1, "Unexpected end of input") { parser.parse("-") }
        assertParseError(2, "Unexpected end of input") { parser.parse("1.") }
        assertParseError(4, "Unexpected end of input") { parser.parse("1.0e") }
        assertParseError(5, "Unexpected end of input") { parser.parse("1.0e-") }
    }

    @Test
    fun `null`() {
        assertThat(dev.botta.json.Json.parse("null")).isEqualTo(dev.botta.json.Json.NULL)
    }

    @Test
    fun `null incomplete`() {
        assertParseError(1, "Unexpected end of input") { parser.parse("n") }
        assertParseError(2, "Unexpected end of input") { parser.parse("nu") }
        assertParseError(3, "Unexpected end of input") { parser.parse("nul") }
    }

    @Test
    fun `null illegal character`() {
        assertParseError(1, "Expected 'u'") { parser.parse("nx") }
        assertParseError(2, "Expected 'l'") { parser.parse("nux") }
        assertParseError(3, "Expected 'l'") { parser.parse("nulx") }
        assertParseError(4, "Unexpected character") { parser.parse("nullx") }
    }

    @Test
    fun `true`() {
        assertThat(dev.botta.json.Json.parse("true")).isEqualTo(dev.botta.json.Json.TRUE)
    }

    @Test
    fun `true incomplete`() {
        assertParseError(1, "Unexpected end of input") { parser.parse("t") }
        assertParseError(2, "Unexpected end of input") { parser.parse("tr") }
        assertParseError(3, "Unexpected end of input") { parser.parse("tru") }
    }

    @Test
    fun `true illegal character`() {
        assertParseError(1, "Expected 'r'") { parser.parse("tx") }
        assertParseError(2, "Expected 'u'") { parser.parse("trx") }
        assertParseError(3, "Expected 'e'") { parser.parse("trux") }
        assertParseError(4, "Unexpected character") { parser.parse("truex") }
    }

    @Test
    fun `false`() {
        assertThat(dev.botta.json.Json.parse("false")).isEqualTo(dev.botta.json.Json.FALSE)
    }

    @Test
    fun `false incomplete`() {
        assertParseError(1, "Unexpected end of input") { parser.parse("f") }
        assertParseError(2, "Unexpected end of input") { parser.parse("fa") }
        assertParseError(3, "Unexpected end of input") { parser.parse("fal") }
        assertParseError(4, "Unexpected end of input") { parser.parse("fals") }
    }

    @Test
    fun `false illegal character`() {
        assertParseError(1, "Expected 'a'") { parser.parse("fx") }
        assertParseError(2, "Expected 'l'") { parser.parse("fax") }
        assertParseError(3, "Expected 's'") { parser.parse("falx") }
        assertParseError(4, "Expected 'e'") { parser.parse("falsx") }
        assertParseError(5, "Unexpected character") { parser.parse("falsex") }
    }

    private fun assertParseError(offset: Int, message: String, block: () -> Unit) {
        val e = assertThrows<JsonParseError>(block)
        assertThat(e.location?.offset).isEqualTo(offset)
        assertThat(e.message).startsWith("$message at")
    }

    private fun loggedLines() = handler.getLog().lines().dropLast(1)

    private val handler = FakeParserHandler()
    private val parser = JsonParser(handler)
}

class FakeParserHandler: JsonParserHandler {
    private val log = StringBuilder()
    override val value = dev.botta.json.Json.NULL
    private var parser: JsonParser? = null
    override val location get() = parser!!.location

    override fun setParser(parser: JsonParser) {
        this.parser = parser
    }

    override fun startNull() {
        record("startNull")
    }

    override fun endNull() {
        record("endNull")
    }

    override fun startBoolean() {
        record("startBoolean")
    }

    override fun endBoolean(bool: Boolean) {
        record("endBoolean", bool)
    }

    override fun startString() {
        record("startString")
    }

    override fun endString(string: String) {
        record("endString", string)
    }

    override fun startNumber() {
        record("startNumber")
    }

    override fun endNumber(string: String) {
        record("endNumber", string)
    }

    override fun startArray(): JsonArray {
        record("startArray")
        return JsonArray()
    }

    override fun endArray(array: JsonArray) {
        record("endArray", array)
    }

    override fun startArrayValue(array: JsonArray) {
        record("startArrayValue", array)
    }

    override fun endArrayValue(array: JsonArray) {
        record("endArrayValue", array)
    }

    override fun startObject(): JsonObject {
        record("startObject")
        return JsonObject()
    }

    override fun endObject(obj: JsonObject) {
        record("endObject", obj)
    }

    override fun startObjectName(obj: JsonObject) {
        record("startObjectName", obj)
    }

    override fun endObjectName(obj: JsonObject, name: String) {
        record("endObjectName", obj, name)
    }

    override fun startObjectValue(obj: JsonObject, name: String) {
        record("startObjectValue", obj, name)
    }

    override fun endObjectValue(obj: JsonObject, name: String) {
        record("endObjectValue", obj, name)
    }

    fun getLog() = log.toString()

    private fun record(event: String, vararg args: Any) {
        log.append(event)
        args.forEach { log.append(' ').append(it) }
        log.append(' ').append(location.offset).append('\n')
    }
}
