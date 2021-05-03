package org.cooltey.punchbabycounter.ui.profile

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.cooltey.punchbabycounter.R
import org.cooltey.punchbabycounter.database.AppDatabase
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileViewModel = ProfileViewModel(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.userList.observe(viewLifecycleOwner, {
            Log.d("Test liveData", it.toString())
        })


        binding.profileBirthday.editText?.setOnFocusChangeListener { v, focused ->
            if (focused) {
                showDatePicker(v)
            }
        }
        binding.profileButtonSave.setOnClickListener {
            save()
        }
    }

    private fun showDatePicker(view: View) {
        DatePickerDialog(
            requireContext(), datePicker(view as EditText),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun datePicker(editText: EditText?): OnDateSetListener {
        return OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editText?.setText(SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.time))
        }
    }

    private fun save() {
        val birthday = "01/17/2020"
        val user = User(firstName = binding.profileFirstName.editText.toString(),
            lastName = binding.profileLastName.editText.toString(),
            nickName = binding.profileNickName.editText.toString(),
            birthday = SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(birthday),
            gender = "M",
            note = binding.profileNote.editText.toString())
//        AppDatabase.db(requireContext()).userDao().insertAll(user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}