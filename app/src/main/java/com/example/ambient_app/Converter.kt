package com.example.ambient_app

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Room cannot store arrays, so we need to convert them JSONArray strings. For this, I decided to
// use Kotlin's serialization plugin.
class Converter {
    // Convert list to string of JSON object
    @TypeConverter
    fun listToString(value: List<String>) = Json.encodeToString(value)

    // Convert JSON string to list of ints
    @TypeConverter
    fun stringToList(value: String) = Json.decodeFromString<List<String>>(value)
}