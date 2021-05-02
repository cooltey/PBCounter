package org.cooltey.punchbabycounter.database

import androidx.room.*

@Database(entities = [User::class, Record::class], version = 1)
@TypeConverters(ConvertUtil::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recordDao(): RecordDao
}
