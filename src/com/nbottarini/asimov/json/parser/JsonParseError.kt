package com.nbottarini.asimov.json.parser

class JsonParseError private constructor(message: String, val location: Location, cause: Throwable? = null):
    Exception(message, cause) {

    constructor(message: String, location: Location): this("$message at $location", location, cause = null)

    constructor(message: String, cause: Throwable? = null): this(message, Location(0, 0, 0), cause)
}
