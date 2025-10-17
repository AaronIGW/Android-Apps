package edu.wcupa.awilliams.booktracker.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toStatus(value: String): BookStatus = BookStatus.valueOf(value)
    @TypeConverter
    fun fromStatus(value: BookStatus): String = value.name
}
