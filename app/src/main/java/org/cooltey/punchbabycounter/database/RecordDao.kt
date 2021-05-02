package org.cooltey.punchbabycounter.database

import androidx.room.*
import java.util.*

@Dao
interface RecordDao {
    @Query("SELECT * FROM record")
    fun getAll(): List<Record>

    @Query("SELECT * FROM record WHERE uid IN (:recordIds)")
    fun loadAllByIds(recordIds: IntArray): List<Record>

    @Query("SELECT * FROM record WHERE user_id = :userId AND date = :date")
    fun findByDate(userId: Int, date: Date): Record

    @Insert
    fun insertAll(vararg records: Record)

    @Delete
    fun delete(record: Record)
}

