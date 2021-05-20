package org.cooltey.punchbabycounter.ui.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.database.Summary
import org.cooltey.punchbabycounter.repository.RecordRepository
import org.cooltey.punchbabycounter.utils.GeneralUtil

class DashboardViewModel(context: Context) : ViewModel() {

    private var recordRepository = RecordRepository(context)

    fun getSummaryByUserId(userId: Long): LiveData<Summary> {
        return recordRepository.getSummaryRecordByUserId(userId)
    }
}