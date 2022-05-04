package com.example.sudoku.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson


class Converters {
    val gson = Gson()
    @TypeConverter
    fun fromString(value: String): Array<Int> {
        return  gson.fromJson(value,Array<Int>::class.java)
    }

    @TypeConverter
    fun fromArrayInt(list: Array<Int>): String {
        return gson.toJson(list)
    }
}