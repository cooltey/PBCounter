package org.cooltey.punchbabycounter.database

import androidx.room.TypeConverter
import java.util.*

class ConvertUtil {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

