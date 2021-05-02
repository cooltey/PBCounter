package org.cooltey.punchbabycounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Record(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "level_1") val level1: Long, // Level 1 punch
    @ColumnInfo(name = "level_2") val level2: Long, // Level 2 punch
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "note") val note: String?
)
