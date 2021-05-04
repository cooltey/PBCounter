package org.cooltey.punchbabycounter.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.cooltey.punchbabycounter.database.AppDatabase
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.database.UserDao

class ProfileRepository(context: Context) {
    fun interface Callback {
        fun onComplete()
    }
    private var userDao = AppDatabase.db(context).userDao()
    private var userList = userDao.getAll()

    fun getUserList(): LiveData<List<User>> {
        return userList
    }

    fun getUserById(id: Int): LiveData<User> {
        return userDao.getById(id)
    }

    fun insertUser(user: User, callback: Callback) {
        Observable.fromCallable { userDao.insertAll(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                callback.onComplete()
            }
    }

    fun updateUser(user: User, callback: Callback) {
        Observable.fromCallable { userDao.updateAll(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                callback.onComplete()
            }
    }
}