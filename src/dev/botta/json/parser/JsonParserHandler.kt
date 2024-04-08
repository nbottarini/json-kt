package dev.botta.json.parser

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

import dev.botta.json.values.*

interface JsonParserHandler {
//    var value: JsonValue? = null
//        protected set
//    var parser: JsonParser? = null
//    val location get() = parser!!.location
    val value: JsonValue
    val location: Location

    fun setParser(parser: JsonParser)

    fun startNull()

    fun endNull()

    fun startBoolean()

    fun endBoolean(bool: Boolean)

    fun startString()

    fun endString(string: String)

    fun startNumber()

    fun endNumber(string: String)

    fun startArray(): JsonArray

    fun endArray(array: JsonArray)

    fun startArrayValue(array: JsonArray)

    fun endArrayValue(array: JsonArray)

    fun startObject(): JsonObject

    fun endObject(obj: JsonObject)

    fun startObjectName(obj: JsonObject)

    fun endObjectName(obj: JsonObject, name: String)

    fun startObjectValue(obj: JsonObject, name: String)

    fun endObjectValue(obj: JsonObject, name: String)
}
