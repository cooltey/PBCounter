package org.cooltey.punchbabycounter.ui.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.database.Summary
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.repository.RecordRepository
import org.cooltey.punchbabycounter.repository.UserRepository
import org.cooltey.punchbabycounter.utils.GeneralUtil

class DashboardViewModel(context: Context) : ViewModel() {

    private var userRepository = UserRepository(context)
    private var recordRepository = RecordRepository(context)

    private val _orderBy = MutableLiveData<Int>().apply {
        value = 0
    }
    val orderBy: LiveData<Int> = _orderBy
    fun updateOrderBy(orderBy: Int) {
        _orderBy.value = orderBy
    }

    fun getUserInfo(userId: Long): LiveData<User> {
        return userRepository.getUserById(userId)
    }

    fun getSummaryByUserId(userId: Long): LiveData<Summary> {
        return recordRepository.getSummaryRecordByUserId(userId)
    }


    fun getList(userId: Long): LiveData<List<Any>> {
        return if (orderBy.value == 0) {
            recordRepository.getRecordsByUserId(userId)
        } else {
            recordRepository.getRecordsByUserIdGroupByMonth(userId)
        } as LiveData<List<Any>>
    }

    fun getListByUserId(userId: Long): LiveData<List<Record>> {
        return recordRepository.getRecordsByUserId(userId)
    }

    fun getListByUserIdGroupByMonth(userId: Long): LiveData<List<Summary>> {
        return recordRepository.getRecordsByUserIdGroupByMonth(userId)
    }
}