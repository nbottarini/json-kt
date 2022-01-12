package com.nbottarini.asimov.json.extensions

import com.nbottarini.asimov.json.Json

fun <T: Any?> List<T>.toJson() = Json.array(this)
