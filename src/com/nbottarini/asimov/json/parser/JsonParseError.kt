package com.nbottarini.asimov.json.parser

class JsonParseError(message: String, val location: Location): Exception("$message at $location")
