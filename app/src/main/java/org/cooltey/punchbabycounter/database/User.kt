package org.cooltey.punchbabycounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "nick_name") val nickName: String?,
    @ColumnInfo(name = "birthday") val birthday: Date?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "note") val note: String?
)
