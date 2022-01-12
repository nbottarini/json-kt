package com.nbottarini.asimov.json.parser

import com.nbottarini.asimov.json.Json
import com.nbottarini.asimov.json.values.*
import com.nbottarini.asimov.json.values.JsonString

class DefaultJsonParserHandler: JsonParserHandler() {
    override fun startArray() = JsonArray()

    override fun startObject() = JsonObject()

    override fun endNull() {
        value = Json.NULL
    }

    override fun endBoolean(bool: Boolean) {
        value = if (bool) Json.TRUE else Json.FALSE
    }

    override fun endString(string: String) {
        value = JsonString(string)
    }

    override fun endNumber(string: String) {
        value = JsonNumber(string)
    }

    override fun endArray(array: JsonArray?) {
        value = array
    }

    override fun endObject(obj: JsonObject?) {
        value = obj
    }

    override fun endArrayValue(array: JsonArray?) {
        array?.add(value!!)
    }

    override fun endObjectValue(obj: JsonObject?, name: String) {
        obj?.set(name, value!!)
    }
}
