package org.cooltey.punchbabycounter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface RecordDao {
    @Query("SELECT * FROM record")
    fun getAll(): LiveData<List<Record>>

    @Query("SELECT * FROM record WHERE user_id IN (:userId) AND (level_1 != 0 OR level_2 != 0) ORDER BY date DESC")
    fun getAllByUserId(userId: Long): LiveData<List<Record>>

    @Query("SELECT SUM(level_1) AS level_1_total, SUM(level_2) AS level_2_total, date as summary_date, STRFTIME('%Y-%m-01',  date(date / 1000, 'unixepoch')) as group_date FROM record WHERE user_id IN (:userId) GROUP BY STRFTIME('%Y-%m-01', group_date) ORDER BY group_date DESC")
    fun getAllByUserIdGroupByMonth(userId: Long): LiveData<List<Summary>>

    @Query("SELECT * FROM record WHERE uid IN (:recordId)")
    fun getById(recordId: Long): LiveData<Record>

    @Query("SELECT SUM(level_1) as level_1_total, SUM(level_2) as level_2_total, date as summary_date FROM record WHERE user_id IN (:userId) ORDER BY (level_1_total + level_2_total) DESC LIMIT 1")
    fun getSummaryById(userId: Long): LiveData<Summary>

    @Query("SELECT * FROM record WHERE user_id = :userId AND date = :date")
    fun findByDate(userId: Long, date: Date): LiveData<Record>

    @Insert
    fun insert(record: Record): Long

    @Update
    fun update(record: Record)

    @Delete
    fun delete(record: Record)
}
