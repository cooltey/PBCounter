package org.cooltey.punchbabycounter.database

import android.content.Context
import androidx.room.*

@Database(entities = [User::class, Record::class], version = 1)
@TypeConverters(ConvertUtil::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recordDao(): RecordDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "database"

        fun db(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
            }
            return INSTANCE as AppDatabase
        }
    }
}
