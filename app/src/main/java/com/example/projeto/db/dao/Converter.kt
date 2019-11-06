package com.example.projeto.db.dao

import android.icu.util.Calendar
import androidx.room.TypeConverter
import java.util.*


object Converters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? {
        return (if (date == null) null else date!!.getTime())?.toLong()
    }

    @TypeConverter
    @JvmStatic
    fun toCalendar(l : Long) : Calendar? {
        var c : Calendar = Calendar.getInstance()
        c.setTimeInMillis(l)
        return c
    }
}