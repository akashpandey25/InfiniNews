package com.project.infininews.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.project.infininews.model.Source

class Converter { //it will convert source object into string
    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name // storing source object to database
    }

    @TypeConverter //string to source
    fun toSource(name: String): Source{
        return Source(name, name) // here 2 times name is passed because in source there is no need of id thats why name is passed 2 times
    }
}