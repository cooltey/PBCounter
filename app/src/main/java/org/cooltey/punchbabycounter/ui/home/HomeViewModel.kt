package org.cooltey.punchbabycounter.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.repository.RecordRepository
import org.cooltey.punchbabycounter.repository.UserRepository
import java.util.*

class HomeViewModel(context: Context) : ViewModel() {

    private var recordRepository = RecordRepository(context)

    val maxCount = 50

    private val _leftCounter = MutableLiveData<Int>().apply {
        value = 0
    }
    val leftCounter: LiveData<Int> = _leftCounter
    fun leftCounterIncrement() {
        _leftCounter.value?.let {
            if (it < maxCount) {
                _leftCounter.value = it + 1
            }
        }
    }

    private val _rightCounter = MutableLiveData<Int>().apply {
        value = 0
    }
    val rightCounter: LiveData<Int> = _rightCounter
    fun rightCounterIncrement() {
        _rightCounter.value?.let {
            if (it < maxCount) {
                _rightCounter.value = it + 1
            }
        }
    }

    fun getRecordByUserId(userId: Long): LiveData<Record> {
        return recordRepository.getRecordByDate(userId, Date())
    }

    fun save(record: Record, callback: RecordRepository.Callback) {
        if (record.uid <= 0) {
            recordRepository.insertRecord(record, callback)
        } else {
            recordRepository.updateRecord(record, callback)
        }
    }
}
