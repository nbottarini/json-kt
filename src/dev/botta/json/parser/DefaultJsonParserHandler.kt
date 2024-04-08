package dev.botta.json.parser

import dev.botta.json.values.*

class DefaultJsonParserHandler: JsonParserHandler {
    override var value: JsonValue = dev.botta.json.Json.NULL
        private set
    private var parser: JsonParser? = null
    override val location get() = parser!!.location

    override fun setParser(parser: JsonParser) {
        this.parser = parser
    }

    override fun startNull() {}

    override fun startArray() = JsonArray()

    override fun startObject() = JsonObject()

    override fun endNull() {
        value = dev.botta.json.Json.NULL
    }

    override fun startBoolean() {}

    override fun endBoolean(bool: Boolean) {
        value = if (bool) dev.botta.json.Json.TRUE else dev.botta.json.Json.FALSE
    }

    override fun startString() {}

    override fun endString(string: String) {
        value = JsonString(string)
    }

    override fun startNumber() {}

    override fun endNumber(string: String) {
        value = JsonNumber(string)
    }

    override fun endArray(array: JsonArray) {
        value = array
    }

    override fun startArrayValue(array: JsonArray) {}

    override fun endObject(obj: JsonObject) {
        value = obj
    }

    override fun startObjectName(obj: JsonObject) {}

    override fun endObjectName(obj: JsonObject, name: String) {}

    override fun startObjectValue(obj: JsonObject, name: String) {}

    override fun endArrayValue(array: JsonArray) {
        array.add(value)
    }

    override fun endObjectValue(obj: JsonObject, name: String) {
        obj[name] = value
    }
}
