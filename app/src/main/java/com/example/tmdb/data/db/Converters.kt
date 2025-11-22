package com.example.tmdb.data.db

import androidx.room.TypeConverter
import com.example.tmdb.data.model.MediaType

class Converters {
    @TypeConverter
    fun toMediaType(value: String): MediaType = MediaType.valueOf(value)

    @TypeConverter
    fun fromMediaType(value: MediaType): String = value.name
}
