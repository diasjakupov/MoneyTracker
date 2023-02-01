package com.example.myapplication.data.transformers

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(value: Long): LocalDate{
        return LocalDate.ofEpochDay(value)
    }


    @TypeConverter
    fun fromDate(value: LocalDate): Long{
        return value.toEpochDay()
    }
}