package com.nbottarini.asimov.json.extensions

import com.nbottarini.asimov.json.Json
import com.nbottarini.asimov.json.writer.WriterConfig

fun <T: Any?> List<T>.toJson() = Json.array(this)

fun <T: Any?> List<T>.toJsonString() = toJson().toString()

fun <T: Any?> List<T>.toJsonString(config: WriterConfig) = toJson().toString(config)
