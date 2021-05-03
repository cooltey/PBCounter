package org.cooltey.punchbabycounter.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel

class ProfileViewModel(context: Context) : ViewModel() {
    private var repo = ProfileRepository(context)
    var userList = repo.getUserList()
}
