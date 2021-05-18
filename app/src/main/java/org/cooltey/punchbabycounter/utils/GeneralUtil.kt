package org.cooltey.punchbabycounter.utils

import android.util.Log
import java.util.*

object GeneralUtil {

    fun getToday(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        return calendar.time
    }

}
