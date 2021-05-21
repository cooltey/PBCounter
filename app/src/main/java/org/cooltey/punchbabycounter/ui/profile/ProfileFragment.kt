package org.cooltey.punchbabycounter.ui.profile

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.cooltey.punchbabycounter.R
import org.cooltey.punchbabycounter.database.User
import org.cooltey.punchbabycounter.databinding.FragmentProfileBinding
import org.cooltey.punchbabycounter.utils.GeneralUtil
import org.cooltey.punchbabycounter.utils.Prefs
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    private var currentUserId = -1L
    private var enableVibration = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileViewModel = ProfileViewModel(requireContext())
        currentUserId = Prefs.getCurrentId(requireActivity())
        enableVibration = Prefs.enableVibration(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (currentUserId > 0) {
           initObservable()
        }

        binding.profileVibration.setOnCheckedChangeListener { _, b ->
            enableVibration = b
        }

        binding.profileBirthday.editText?.setOnFocusChangeListener { v, focused ->
            if (focused) {
                showDatePicker(v)
            }
        }

        binding.profileButtonSave.setOnClickListener {
            save()
        }
    }

    private fun initObservable() {
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
                binding.profileVibration.isChecked = enableVibration
                currentUserId = it.uid
            }
        })
    }

    override fun onResume() {
        super.onResume()
        showViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showViews() {
        if (currentUserId <= 0) {
            binding.newProfileButton.visibility = View.VISIBLE
            binding.scrollViewContainer.visibility = View.GONE
            binding.newProfileButton.setOnClickListener {
                binding.newProfileButton.visibility = View.GONE
                binding.scrollViewContainer.visibility = View.VISIBLE
            }
        } else {
            binding.newProfileButton.visibility = View.GONE
            binding.scrollViewContainer.visibility = View.VISIBLE
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
        val user = User(firstName = GeneralUtil.getEditString(binding.profileFirstName),
            lastName = GeneralUtil.getEditString(binding.profileLastName),
            nickName = GeneralUtil.getEditString(binding.profileNickName),
            birthday = dateFormat.parse(GeneralUtil.getEditString(binding.profileBirthday)),
            gender = gender,
            note = GeneralUtil.getEditString(binding.profileNote))

        // Update
        if (currentUserId > 0) {
            user.uid = currentUserId
        }

        profileViewModel.save(user) {
            Toast.makeText(requireContext(), R.string.toast_success, Toast.LENGTH_SHORT).show()
            Prefs.saveCurrentId(requireActivity(), it)
            Prefs.enableVibration(requireActivity(), enableVibration)
        }
    }
}
