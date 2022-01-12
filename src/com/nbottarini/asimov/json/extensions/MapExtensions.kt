package com.nbottarini.asimov.json.extensions

import com.nbottarini.asimov.json.Json

fun <T: Any?> Map<String, T>.toJson() = Json.value(this)
