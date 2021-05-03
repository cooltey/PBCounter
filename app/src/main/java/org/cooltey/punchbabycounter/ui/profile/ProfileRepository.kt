package org.cooltey.punchbabycounter.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import org.cooltey.punchbabycounter.database.AppDatabase
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.database.UserDao

class ProfileRepository(context: Context) {
    private var userDao: UserDao = AppDatabase.db(context).userDao()
    private var userList: LiveData<List<User>> = userDao.getAll()

    fun getUserList(): LiveData<List<User>> {
        return userList
    }
}