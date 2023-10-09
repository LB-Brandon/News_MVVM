package com.brandon.news_mvvm.db

import androidx.room.TypeConverter

import com.brandon.news_mvvm.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

}