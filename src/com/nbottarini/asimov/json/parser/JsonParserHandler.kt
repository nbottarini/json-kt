package com.nbottarini.asimov.json.parser

/*
Copyright (c) 2013, 2014 EclipseSource

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import com.nbottarini.asimov.json.values.JsonArray
import com.nbottarini.asimov.json.values.JsonObject
import com.nbottarini.asimov.json.values.JsonValue

abstract class JsonParserHandler {
    var value: JsonValue? = null
        protected set
    var parser: JsonParser? = null
    val location get() = parser!!.location

    open fun startNull() {}

    open fun endNull() {}

    open fun startBoolean() {}

    open fun endBoolean(bool: Boolean) {}

    open fun startString() {}

    open fun endString(string: String) {}

    open fun startNumber() {}

    open fun endNumber(string: String) {}

    open fun startArray(): JsonArray? {
        return null
    }

    open fun endArray(array: JsonArray?) {}

    open fun startArrayValue(array: JsonArray?) {}

    open fun endArrayValue(array: JsonArray?) {}

    open fun startObject(): JsonObject? {
        return null
    }

    open fun endObject(obj: JsonObject?) {}

    open fun startObjectName(obj: JsonObject?) {}

    open fun endObjectName(obj: JsonObject?, name: String) {}

    open fun startObjectValue(obj: JsonObject?, name: String) {}

    open fun endObjectValue(obj: JsonObject?, name: String) {}
}
