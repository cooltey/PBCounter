package org.cooltey.punchbabycounter.utils

import android.app.Activity
import android.content.Context
import org.cooltey.punchbabycounter.R

object Prefs {

    fun saveCurrentId(activity: Activity, userId: Long) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putLong(activity.getString(R.string.prefs_current_id), userId)
            apply()
        }
    }

    fun getCurrentId(activity: Activity): Long {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return -1
        return sharedPref.getLong(activity.getString(R.string.prefs_current_id), -1)
    }

}
