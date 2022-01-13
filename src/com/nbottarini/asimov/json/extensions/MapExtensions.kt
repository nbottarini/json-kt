package com.nbottarini.asimov.json.extensions

import com.nbottarini.asimov.json.Json
import com.nbottarini.asimov.json.writer.WriterConfig

fun <T: Any?> Map<String, T>.toJson() = Json.value(this).asObject()!!

fun <T: Any?> Map<String, T>.toJsonString() = toJson().toString()

fun <T: Any?> Map<String, T>.toJsonString(config: WriterConfig) = toJson().toString(config)
