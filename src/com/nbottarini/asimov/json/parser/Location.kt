package com.nbottarini.asimov.json.parser

class Location(val offset: Int, val line: Int, val column: Int) {
    override fun toString() = "$line:$column"

    override fun hashCode() = offset

    override fun equals(other: Any?) =
        other is Location && other.offset == offset && other.line == line && other.column == column
}
