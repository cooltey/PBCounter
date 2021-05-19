package org.cooltey.punchbabycounter.utils

import com.google.android.material.textfield.TextInputLayout
import java.util.*

object GeneralUtil {

    fun getToday(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getEditString(view: TextInputLayout): String {
        return view.editText?.text.toString()
    }
}
