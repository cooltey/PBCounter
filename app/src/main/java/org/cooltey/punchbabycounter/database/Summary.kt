package org.cooltey.punchbabycounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Summary(
    @ColumnInfo(name = "level_1_total") val level1Total: Long,
    @ColumnInfo(name = "level_2_total") val level2Total: Long,
    @ColumnInfo(name = "summary_date") val summaryDate: Date
)
