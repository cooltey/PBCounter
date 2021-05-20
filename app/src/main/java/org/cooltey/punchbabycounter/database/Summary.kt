package org.cooltey.punchbabycounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Summary(
    @ColumnInfo(name = "level_1_total") val level1_total: Long,
    @ColumnInfo(name = "level_2_total") val level2_total: Long,
    @ColumnInfo(name = "date") val date: Date
)
