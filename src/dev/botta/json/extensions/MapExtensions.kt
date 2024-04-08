package dev.botta.json.extensions

import dev.botta.json.writer.WriterConfig

fun <T: Any?> Map<String, T>.toJson() = dev.botta.json.Json.value(this).asObject()!!

fun <T: Any?> Map<String, T>.toJsonString() = toJson().toString()

fun <T: Any?> Map<String, T>.toJsonString(config: WriterConfig) = toJson().toString(config)
