package com.androiddevs.mvvmnewsapp.db

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.models.Source

/*
* We need to convert Source class (shown below) to string and visa versa
* data class Source(
    val id: String,
    val name: String
)
* we do not need the id field, so we do not convert it
* */

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name) // name twice because we don't care about id
    }
}