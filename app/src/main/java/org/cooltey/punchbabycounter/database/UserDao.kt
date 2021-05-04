package org.cooltey.punchbabycounter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE uid IN (:userId)")
    fun getById(userId: Int): LiveData<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Update
    fun updateAll(vararg users: User)

    @Delete
    fun delete(user: User)
}

