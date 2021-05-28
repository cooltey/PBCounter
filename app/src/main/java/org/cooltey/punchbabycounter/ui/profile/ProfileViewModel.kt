package org.cooltey.punchbabycounter.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.repository.UserRepository

class ProfileViewModel(context: Context) : ViewModel() {

    private var repo = UserRepository(context)

    var showList = true

    var getUserList = repo.getUserList()

    fun getUserById(id: Long): LiveData<User> {
        return repo.getUserById(id)
    }

    fun save(user: User, callback: UserRepository.Callback) {
        if (user.uid <= 0) {
            repo.insertUser(user, callback)
        } else {
            repo.updateUser(user, callback)
        }
    }
}
