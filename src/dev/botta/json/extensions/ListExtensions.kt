package dev.botta.json.extensions

import dev.botta.json.writer.WriterConfig

fun <T: Any?> List<T>.toJson() = dev.botta.json.Json.array(this)

fun <T: Any?> List<T>.toJsonString() = toJson().toString()

fun <T: Any?> List<T>.toJsonString(config: WriterConfig) = toJson().toString(config)
