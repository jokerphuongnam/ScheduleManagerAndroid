package com.pnam.schedulemanager.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    private val gson: Gson by lazy { Gson() }

    @TypeConverter
    fun fromStrings(strings: List<String>): String =
        gson.toJson(strings, object : TypeToken<List<String>>() {}.type)


    @TypeConverter
    fun toStrings(json: String): List<String> = gson.fromJson(
        json,
        object : TypeToken<List<String>>() {}.type
    )

    @TypeConverter
    fun fromDates(dates: List<Long>): String =
        gson.toJson(dates, object : TypeToken<List<Long>>() {}.type)

    @TypeConverter
    fun toDates(json: String): List<Long> = gson.fromJson(
        json,
        object : TypeToken<List<Long>>() {}.type
    )
}