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
import android.widget.Toast
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
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    private var currentUserId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileViewModel = ProfileViewModel(requireContext())
        currentUserId = 1 // TODO: select the activated one using sharedElement
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getUserById(currentUserId).observe(viewLifecycleOwner, {
            it?.let {
                binding.profileFirstName.editText?.setText(it.firstName)
                binding.profileLastName.editText?.setText(it.lastName)
                binding.profileNickName.editText?.setText(it.nickName)
                it.birthday?.let { date ->
                    binding.profileBirthday.editText?.setText(dateFormat.format(date))
                    calendar.timeInMillis = date.time
                }
                it.gender?.let { gender ->
                    if (gender == "M") {
                        binding.profileGenderMale.isChecked = true
                    } else {
                        binding.profileGenderFemale.isChecked = true
                    }
                }
                binding.profileNote.editText?.setText(it.note)
                currentUserId = it.uid
            }
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
            editText?.setText(dateFormat.format(calendar.time))
        }
    }

    private fun save() {
        var gender = "M"
        if (binding.profileGenderFemale.isChecked) {
            gender = "F"
        }
        val user = User(uid = currentUserId,
            firstName = getString(binding.profileFirstName),
            lastName = getString(binding.profileLastName),
            nickName = getString(binding.profileNickName),
            birthday = dateFormat.parse(getString(binding.profileBirthday)),
            gender = gender,
            note = getString(binding.profileNote))

        profileViewModel.save(user) { Toast.makeText(requireContext(), R.string.toast_success, Toast.LENGTH_SHORT).show() }
    }

    private fun getString(view: TextInputLayout): String {
        return view.editText?.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}