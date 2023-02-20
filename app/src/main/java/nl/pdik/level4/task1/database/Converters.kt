package nl.pdik.level4.task1.database

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun dateFromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}