package org.cooltey.punchbabycounter.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.cooltey.punchbabycounter.database.AppDatabase
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.database.User
import java.util.*

class RecordRepository(context: Context) {
    fun interface Callback {
        fun onComplete(recordId: Long)
    }
    private var recordDao = AppDatabase.db(context).recordDao()
    private var recordList = recordDao.getAll()

    fun getRecordsList(): LiveData<List<Record>> {
        return recordList
    }

    fun getRecordsByUserId(userId: Long): LiveData<List<Record>> {
        return recordDao.getAllByUserId(userId)
    }

    fun getRecordById(id: Long): LiveData<Record> {
        return recordDao.getById(id)
    }

    fun getRecordByDate(userId: Long, date: Date): LiveData<Record> {
        return recordDao.findByDate(userId, date)
    }

    fun insertRecord(record: Record, callback: Callback) {
        Observable.fromCallable { recordDao.insert(record) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onComplete(it)
            }, { catch ->
                Log.d("insertRecord", catch.toString())
            })
    }

    fun updateRecord(record: Record, callback: Callback) {
        Observable.fromCallable { recordDao.update(record) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onComplete(record.uid)
            }, { catch ->
                Log.d("updateRecord", catch.toString())
            })
    }
}