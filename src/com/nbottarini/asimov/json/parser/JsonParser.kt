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
import java.io.Reader
import java.io.StringReader
import kotlin.math.max
import kotlin.math.min

/**
 * A streaming parser for JSON text. The parser reports all events to a given handler.
 */
class JsonParser(private val handler: JsonParserHandler = DefaultJsonParserHandler()) {
    private var reader: Reader? = null
    private lateinit var buffer: CharArray
    private var bufferOffset = 0
    private var index = 0
    private var fill = 0
    private var line = 0
    private var lineOffset = 0
    private var current = 0
    private var captureBuffer: StringBuilder? = null
    private var captureStart = 0
    private var nestingLevel = 0
    val location: Location
        get() {
            val offset = bufferOffset + index - 1
            val column = offset - lineOffset + 1
            return Location(offset, line, column)
        }
  /*
   * |                      bufferOffset
   *                        v
   * [a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t]        < input
   *                       [l|m|n|o|p|q|r|s|t|?|?]    < buffer
   *                          ^               ^
   *                       |  index           fill
   */

    init {
        handler.parser = this
    }

    fun parse(string: String): JsonValue? {
        val bufferSize = max(MIN_BUFFER_SIZE, min(DEFAULT_BUFFER_SIZE, string.length))
        parse(StringReader(string), bufferSize)
        return handler.value
    }

    fun parse(reader: Reader, buffersize: Int = DEFAULT_BUFFER_SIZE): JsonValue? {
        if (buffersize <= 0) throw IllegalArgumentException("buffersize is zero or negative")
        this.reader = reader
        buffer = CharArray(buffersize)
        bufferOffset = 0
        index = 0
        fill = 0
        line = 1
        lineOffset = 0
        current = 0
        captureStart = -1
        read()
        skipWhiteSpace()
        readValue()
        skipWhiteSpace()
        if (!isEndOfText) throw error("Unexpected character")
        return handler.value
    }

    private fun readValue() {
        when (current.toChar()) {
            'n' -> readNull()
            't' -> readTrue()
            'f' -> readFalse()
            '"' -> readString()
            '[' -> readArray()
            '{' -> readObject()
            '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> readNumber()
            else -> throw expected("value")
        }
    }

    private fun readArray() {
        val array: JsonArray? = handler.startArray()
        read()
        if (++nestingLevel > MAX_NESTING_LEVEL) throw error("Nesting too deep")
        skipWhiteSpace()
        if (readChar(']')) {
            nestingLevel--
            handler.endArray(array)
            return
        }
        do {
            skipWhiteSpace()
            handler.startArrayValue(array)
            readValue()
            handler.endArrayValue(array)
            skipWhiteSpace()
        } while (readChar(','))
        if (!readChar(']')) throw expected("',' or ']'")
        nestingLevel--
        handler.endArray(array)
    }

    private fun readObject() {
        val obj: JsonObject? = handler.startObject()
        read()
        if (++nestingLevel > MAX_NESTING_LEVEL) throw error("Nesting too deep")
        skipWhiteSpace()
        if (readChar('}')) {
            nestingLevel--
            handler.endObject(obj)
            return
        }
        do {
            skipWhiteSpace()
            handler.startObjectName(obj)
            val name: String = readName()
            handler.endObjectName(obj, name)
            skipWhiteSpace()
            if (!readChar(':')) throw expected("':'")
            skipWhiteSpace()
            handler.startObjectValue(obj, name)
            readValue()
            handler.endObjectValue(obj, name)
            skipWhiteSpace()
        } while (readChar(','))
        if (!readChar('}'))  throw expected("',' or '}'")
        nestingLevel--
        handler.endObject(obj)
    }

    private fun readName(): String {
        if (current != '"'.code) throw expected("name")
        return readStringInternal()
    }

    private fun readNull() {
        handler.startNull()
        read()
        readRequiredChar('u')
        readRequiredChar('l')
        readRequiredChar('l')
        handler.endNull()
    }

    private fun readTrue() {
        handler.startBoolean()
        read()
        readRequiredChar('r')
        readRequiredChar('u')
        readRequiredChar('e')
        handler.endBoolean(true)
    }

    private fun readFalse() {
        handler.startBoolean()
        read()
        readRequiredChar('a')
        readRequiredChar('l')
        readRequiredChar('s')
        readRequiredChar('e')
        handler.endBoolean(false)
    }

    private fun readRequiredChar(ch: Char) {
        if (!readChar(ch)) throw expected("'$ch'")
    }

    private fun readString() {
        handler.startString()
        handler.endString(readStringInternal())
    }

    private fun readStringInternal(): String {
        read()
        startCapture()
        while (current != '"'.code) {
            if (current == '\\'.code) {
                pauseCapture()
                readEscape()
                startCapture()
            } else if (current < 0x20) {
                throw expected("valid string character")
            } else {
                read()
            }
        }
        val string = endCapture()
        read()
        return string
    }

    private fun readEscape() {
        read()
        when (current.toChar()) {
            '"', '/', '\\' -> captureBuffer!!.append(current.toChar())
            'b' -> captureBuffer!!.append('\b')
//            'f' -> captureBuffer!!.append('\f')
            'n' -> captureBuffer!!.append('\n')
            'r' -> captureBuffer!!.append('\r')
            't' -> captureBuffer!!.append('\t')
            'u' -> {
                val hexChars = CharArray(4)
                var i = 0
                while (i < 4) {
                    read()
                    if (!isHexDigit) throw expected("hexadecimal digit")
                    hexChars[i] = current.toChar()
                    i++
                }
                captureBuffer!!.append(String(hexChars).toInt(16).toChar())
            }
            else -> throw expected("valid escape sequence")
        }
        read()
    }

    private fun readNumber() {
        handler.startNumber()
        startCapture()
        readChar('-')
        val firstDigit: Int = current
        if (!readDigit()) throw expected("digit")
        if (firstDigit != '0'.code) {
            while (readDigit()) {}
        }
        readFraction()
        readExponent()
        handler.endNumber(endCapture())
    }

    private fun readFraction(): Boolean {
        if (!readChar('.')) {
            return false
        }
        if (!readDigit()) throw expected("digit")
        while (readDigit()) {}
        return true
    }

    private fun readExponent(): Boolean {
        if (!readChar('e') && !readChar('E')) {
            return false
        }
        if (!readChar('+')) {
            readChar('-')
        }
        if (!readDigit()) throw expected("digit")
        while (readDigit()) {}
        return true
    }

    private fun readChar(ch: Char): Boolean {
        if (current != ch.code) return false
        read()
        return true
    }

    private fun readDigit(): Boolean {
        if (!isDigit) return false
        read()
        return true
    }

    private fun skipWhiteSpace() {
        while (isWhiteSpace) {
            read()
        }
    }

    private fun read() {
        if (index == fill) {
            if (captureStart != -1) {
                captureBuffer!!.append(buffer, captureStart, fill - captureStart)
                captureStart = 0
            }
            bufferOffset += fill
            fill = reader!!.read(buffer, 0, buffer.size)
            index = 0
            if (fill == -1) {
                current = -1
                index++
                return
            }
        }
        if (current == '\n'.code) {
            line++
            lineOffset = bufferOffset + index
        }
        current = buffer[index++].code
    }

    private fun startCapture() {
        if (captureBuffer == null) {
            captureBuffer = StringBuilder()
        }
        captureStart = index - 1
    }

    private fun pauseCapture() {
        val end = if (current == -1) index else index - 1
        captureBuffer!!.append(buffer, captureStart, end - captureStart)
        captureStart = -1
    }

    private fun endCapture(): String {
        val start = captureStart
        val end = index - 1
        captureStart = -1
        if (captureBuffer!!.isNotEmpty()) {
            captureBuffer!!.append(buffer, start, end - start)
            val captured = captureBuffer.toString()
            captureBuffer!!.setLength(0)
            return captured
        }
        return String(buffer, start, end - start)
    }

    private fun expected(expected: String): JsonParseError {
        if (isEndOfText) return error("Unexpected end of input")
        return error("Expected $expected")
    }

    private fun error(message: String) = JsonParseError(message, location)

    private val isWhiteSpace
        get() = (current == ' '.code) || (current == '\t'.code) || (current == '\n'.code) || (current == '\r'.code)

    private val isDigit get() = current >= '0'.code && current <= '9'.code

    private val isHexDigit
        get() = (current >= '0'.code && current <= '9'.code
                ) || (current >= 'a'.code && current <= 'f'.code
                ) || (current >= 'A'.code && current <= 'F'.code)

    private val isEndOfText get() = current == -1

    companion object {
        private const val MAX_NESTING_LEVEL = 1000
        private const val MIN_BUFFER_SIZE = 10
        private const val DEFAULT_BUFFER_SIZE = 1024
    }
}
