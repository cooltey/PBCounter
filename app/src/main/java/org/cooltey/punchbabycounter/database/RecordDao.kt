package org.cooltey.punchbabycounter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface RecordDao {
    @Query("SELECT * FROM record")
    fun getAll(): LiveData<List<Record>>

    @Query("SELECT * FROM record WHERE user_id IN (:userId)")
    fun getAllByUserId(userId: Long): LiveData<List<Record>>

    @Query("SELECT * FROM record WHERE uid IN (:recordId)")
    fun getById(recordId: Long): LiveData<Record>

    @Query("SELECT * FROM record WHERE user_id = :userId AND date = :date")
    fun findByDate(userId: Long, date: Date): LiveData<Record>

    @Insert
    fun insert(vararg records: Record): Long

    @Update
    fun update(record: Record)

    @Delete
    fun delete(record: Record)
}

