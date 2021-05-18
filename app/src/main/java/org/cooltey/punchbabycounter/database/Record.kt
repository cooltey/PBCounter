package org.cooltey.punchbabycounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "level_1") val level1: Long, // Level 1 punch
    @ColumnInfo(name = "level_2") val level2: Long, // Level 2 punch
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "note") var note: String? = null
)
