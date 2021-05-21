package org.cooltey.punchbabycounter.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.repository.RecordRepository
import org.cooltey.punchbabycounter.repository.UserRepository
import org.cooltey.punchbabycounter.utils.GeneralUtil

class HomeViewModel(context: Context) : ViewModel() {

    private var userRepository = UserRepository(context)
    private var recordRepository = RecordRepository(context)

    val maxCount = 50

    fun getUserInfo(userId: Long): LiveData<User> {
        return userRepository.getUserById(userId)
    }

    private val _leftCounter = MutableLiveData<Long>().apply {
        value = 0
    }
    val leftCounter: LiveData<Long> = _leftCounter
    fun leftCounterIncrement() {
        _leftCounter.value?.let {
            _leftCounter.value = it + 1
        }
    }

    private val _rightCounter = MutableLiveData<Long>().apply {
        value = 0
    }
    val rightCounter: LiveData<Long> = _rightCounter
    fun rightCounterIncrement() {
        _rightCounter.value?.let {
            _rightCounter.value = it + 1
        }
    }

    fun resetCounters() {
        _leftCounter.value = 0
        _rightCounter.value = 0
    }

    private val _recordNote = MutableLiveData<String>().apply {
        value = null
    }
    val recordNote: LiveData<String> = _recordNote
    fun updateRecordNote(text: String) {
        _recordNote.value = text
    }

    fun getRecordByUserId(userId: Long): LiveData<Record> {
        return recordRepository.getRecordByDate(userId, GeneralUtil.getToday())
    }

    fun save(record: Record, callback: RecordRepository.Callback) {
        if (record.uid <= 0) {
            recordRepository.insertRecord(record, callback)
        } else {
            recordRepository.updateRecord(record, callback)
        }
    }
}
