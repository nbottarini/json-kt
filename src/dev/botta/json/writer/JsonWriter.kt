package dev.botta.json.writer

interface JsonWriter {
    fun writeLiteral(value: String)
    fun writeNumber(string: String)
    fun writeString(string: String)
    fun writeArrayOpen()
    fun writeArrayClose()
    fun writeArraySeparator()
    fun writeObjectOpen()
    fun writeObjectClose()
    fun writeMemberSeparator()
    fun writeObjectSeparator()
}
