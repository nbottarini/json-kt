[![Maven](https://img.shields.io/maven-central/v/com.nbottarini/asimov-json.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.nbottarini%22%20AND%20a%3A%22asimov-json%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![CI Status](https://github.com/nbottarini/asimov-json-kt/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/nbottarini/asimov-json-kt/actions?query=branch%3Amaster+workflow%3Aci)

# Asimov Json
Small and fast kotlin library for reading and writing json.

## Installation

#### Gradle (Kotlin)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.nbottarini:asimov-json:0.5")
}
```

#### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.nbottarini:asimov-json:0.5'
}
```

#### Maven

```xml
<dependency>
    <groupId>com.nbottarini</groupId>
    <artifactId>asimov-json</artifactId>
    <version>0.5</version>
</dependency>
```

## Usage

Given the following sample json:

```json
{
  "name": "John",
  "lastname": "Hanks",
  "purchases": [
    {
      "id": 1,
      "productName": "Gaming Keyboard",
      "price": 200.5
    },
    {
      "id": 1,
      "productName": "Razer Headset",
      "price": 79.9
    }
  ]
}
```

```kotlin
val json = Json.parse(input) // input can be a string with your json or a java.io.Reader

val name = json.path("name").asString() // returns "John"
val keyboardPrice = json.path("purchases[0].price").asFloat() // returns 200.5

val name = json.path("name").asBoolean() // returns null
val name = json.path("name").asBoolean("some-default") // returns "some-default"
```

### Json class

The Json class is the main entrypoint to the library. It allows you to parse and build json.

All json values are represented with the class JsonValue. There are many possible types of values (each one represented in
its own class that inherits from JsonValue):
- Ints, Longs, Floats, Doubles are represented in JsonNumber
- Null, True, False are represented in JsonLiteral
- Strings are represented in JsonString
- Arrays are represented in JsonArray
- Objects are represented in JsonObject

The Json class allows to build instances of each type:

```kotlin
// Boolean values
Json.TRUE
Json.value(true)
Json.FALSE
Json.value(false)

// Null value
Json.NULL
Json.value(null)

// Numeric values
Json.value(50)
Json.value(50L)
Json.value(3.14f)
Json.value(3.14)

// Strings
Json.value("Some string")

// Arrays
Json.value(listOf(1, 2, 3))
Json.array(1, 2, 3)
Json.array(true, "some string", 3.14)
Json.array(1, Json.array(2, 3))
listOf(1, 2, 3).toJson()

// Objects
Json.value(mapOf("name" to "John", "lastname" to "Lennon"))
Json.obj("name" to "John", "lastname" to "Lennon")
Json.obj("name" to "John", "emails" to Json.array("email1@example.com", "email1@example.com"))
Json.obj("name" to "John", "emails" to listOf("email1@example.com", "email1@example.com"))
mapOf("name" to "John", "lastname" to "Lennon").toJson()
```

### Json values

Json values can be converted to string:

```kotlin
Json.obj("name" to "John", "lastname" to "Lennon").toString()
// returns {"name":"John","lastname":"Lennon"}

Json.obj("name" to "John", "lastname" to "Lennon").toPrettyString()
// returns:
// { 
//   "name": "John",
//   "lastname": "Lennon"
// }

// Pretty printing can be customized:
Json.array(1, 2, 3).toString(PrettyPrint.indentWithSpaces(2))
Json.array(1, 2, 3).toString(PrettyPrint.indentWithTabs())
Json.array(1, 2, 3).toString(PrettyPrint.singleLine())

// You can convert lists and maps directly to json strings
listOf(1, 2, 3).toJsonString()
mapOf("name" to "John", "lastname" to "Lennon").toJsonString()
```

Json values allows to convert the value to Java/Kotlin types:

```kotlin
Json.value(23).asInt()
Json.value(23L).asLong()
Json.value(3.14f).asFloat()
Json.value(3.14).asDouble()
Json.value("foo").asString()
Json.value(true).asBoolean()
```

Value conversion returns null if the value is incompatible. You can also provide a default value in that case:
```kotlin
Json.value(true).asInt() // returns null
Json.value(true).asInt(23) // returns default value 23

Json.value("23").asInt() // returns 23
Json.value(23L).asInt() // returns 23
```

You can also cast to JsonArray and JsonObject to traverse the json tree:
```kotlin
json.asObject()["name"]
json.asArray()[2]
```

Or you can use the path method for traversing:
```kotlin
json.path("user.purchases[2].product.price").asFloat()
```

You can also assert for each type:
```kotlin
json.isObject()
json.isArray()
json.isNumber()
json.isString()
json.isBoolean()
json.isTrue()
json.isFalse()
json.isNull()
```

### Json arrays

JsonArray implements MutableList<JsonValue> interface, so you can have all common list behaviours.

```kotlin
val array = Json.array(1, 2, 3)

array.size() // 3
array.isEmpty() // false
array.add(4)
array.add(1, 4) // Adds 4 at index 1
array.addAll(listOf(4, 5, 6))
array.addAll(1, listOf(4, 5, 6)) // Adds values 4, 5, 6 at index 1
array[0] // returns 1
array.get(0)
array[0] = 2
array.set(0, 2)
array.with(4).with(5).with(6) // Adds values with a fluent interface
array.with(4, 5, 6)
array.remove(2) // Removes value 2
array.removeAll(listOf(2, 3))
array.retainAll(listOf(1, 2))
array.clear()
array.contains(2)
array.containsAll(listOf(1, 2))
array.indexOf(3)
array.lastIndexOf(2)
```

You can also convert lists to arrays:
```kotlin
listOf(1, 2, 3).toJson()
Json.array(listOf(1, 2, 3))
```

### Json objects
JsonObject implements MutableMap<String, JsonValue> interface, so you can have all common map behaviours.

```kotlin
val obj = Json.obj("name" to "John", "lastname" to "Lennon")

obj.size() // 2
obj.isEmpty() // false
obj.entries() // List of map entries
obj.keys() // name, lastname
obj.values() // John, Lennon
obj["name"] // "John"
obj.get("name")
obj["name"] = "Paul"
obj.set("name", "Paul")
obj.with("age" to 40) // add values with fluent interface
obj.with("age" to 40, "instrument" to "guitar")
obj.merge(Json.obj("name" to "Paul", "instrument" to "bass")) // Merges values with another object
obj.containsKey("name")
obj.containsValue("Paul")
obj.remove("name")
obj.clear()
```

You can also convert maps to objects:
```kotlin
mapOf("name" to "John", "lastname" to "Lennon").toJson()
Json.obj(listOf("name" to "John", "lastname" to "Lennon"))
```
