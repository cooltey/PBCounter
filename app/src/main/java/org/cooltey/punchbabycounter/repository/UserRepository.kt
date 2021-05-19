package org.cooltey.punchbabycounter.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.cooltey.punchbabycounter.database.AppDatabase
import org.cooltey.punchbabycounter.database.User

class UserRepository(context: Context) {
    fun interface Callback {
        fun onComplete(userId: Long)
    }
    private var userDao = AppDatabase.db(context).userDao()
    private var userList = userDao.getAll()

    fun getUserList(): LiveData<List<User>> {
        return userList
    }

    fun getUserById(id: Long): LiveData<User> {
        return userDao.getById(id)
    }

    fun insertUser(user: User, callback: Callback) {
        Observable.fromCallable { userDao.insert(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onComplete(it)
            }, { catch ->
                Log.d("insertUser", catch.toString())
            })
    }

    fun updateUser(user: User, callback: Callback) {
        Observable.fromCallable { userDao.update(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onComplete(user.uid)
            }, { catch ->
                Log.d("updateUser", catch.toString())
            })
    }
}
