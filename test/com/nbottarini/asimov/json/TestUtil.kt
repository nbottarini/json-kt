package com.nbottarini.asimov.json

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object TestUtil {
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> serializeAndDeserialize(instance: T): T {
        return deserialize(serialize(instance)) as T
    }

    private fun serialize(obj: Any): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        ObjectOutputStream(outputStream).writeObject(obj)
        return outputStream.toByteArray()
    }

    private fun deserialize(bytes: ByteArray?): Any {
        val inputStream = ByteArrayInputStream(bytes)
        return ObjectInputStream(inputStream).readObject()
    }
}
