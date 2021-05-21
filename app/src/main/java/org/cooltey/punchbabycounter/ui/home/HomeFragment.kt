package org.cooltey.punchbabycounter.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import org.cooltey.punchbabycounter.MainActivity
import org.cooltey.punchbabycounter.R
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.databinding.FragmentHomeBinding
import org.cooltey.punchbabycounter.utils.GeneralUtil
import org.cooltey.punchbabycounter.utils.Prefs
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private var currentUserId = -1L
    private var recordData: Record? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = HomeViewModel(requireContext())
        currentUserId = Prefs.getCurrentId(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentUserId <= 0) {
            (requireActivity() as MainActivity).goToProfileTab()
        }

        binding.todayDate.text = showToday()

        homeViewModel.getRecordByUserId(currentUserId).observe(viewLifecycleOwner, { record ->
            recordData = record

            binding.leftClickCounter.text = getString(R.string.home_left_level_prefix, GeneralUtil.getFormattedNumber(record?.level1 ?: 0))
            binding.rightClickCounter.text = getString(R.string.home_right_level_prefix, GeneralUtil.getFormattedNumber(record?.level2 ?: 0))
            record?.let {
                binding.recordNote.editText?.setText(it.note, TextView.BufferType.EDITABLE)
                homeViewModel.updateRecordNote(it.note.orEmpty())
            }

            binding.touchCircleLeft.setOnClickListener {
                homeViewModel.leftCounterIncrement()
                homeViewModel.leftCounter.observe(viewLifecycleOwner, {
                    binding.leftClickCounter.text = getString(R.string.home_left_level_prefix, GeneralUtil.getFormattedNumber(record.level1 + it))
                    gradientCircleUpdate(it, binding.touchCircleLeft)
                    vibratePhone(200)
                })
            }

            binding.touchCircleRight.setOnClickListener {
                homeViewModel.rightCounterIncrement()
                homeViewModel.rightCounter.observe(viewLifecycleOwner, {
                    binding.rightClickCounter.text = getString(R.string.home_right_level_prefix, GeneralUtil.getFormattedNumber(record.level2 + it))
                    gradientCircleUpdate(it, binding.touchCircleRight)
                    vibratePhone(400)
                })
            }

            binding.recordNote.editText?.addTextChangedListener {
                homeViewModel.updateRecordNote(it.toString())
            }
        })
    }

    override fun onPause() {
        super.onPause()
        save()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToday(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun save() {
        if (currentUserId <= 0) {
            return
        }
        // TODO: not sure it is a proper way of doing multiple observe.
        homeViewModel.leftCounter.observe(viewLifecycleOwner, { leftCounter ->
            homeViewModel.rightCounter.observe(viewLifecycleOwner, { rightCounter ->
                homeViewModel.recordNote.observe(viewLifecycleOwner, { recordNote ->
                    val newRecordData = buildRecordData(leftCounter, rightCounter, recordNote)
                    if (newRecordData != recordData) {
                        homeViewModel.save(newRecordData) {
                            // ignore
                        }
                    }
                })
            })
        })
    }

    // TODO: have flexible levels
    private fun buildRecordData(level1: Long = 0, level2: Long = 0, note: String? = null): Record {
        return recordData?.let {
            Record(uid = it.uid,
                userId = currentUserId,
                level1 = level1 + it.level1,
                level2 = level2 + it.level2,
                date = it.date,
                note = note)
        } ?: run {
            Record(userId = currentUserId,
                level1 = level1,
                level2 = level2,
                date = GeneralUtil.getToday(),
                note = note)
        }
    }

    private fun vibratePhone(vibrateDuration: Long) {
        if (Prefs.enableVibration(requireActivity())) {
            val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        vibrateDuration,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(vibrateDuration)
            }
        }
    }

    private fun gradientCircleUpdate(counter: Long, view: View) {
        view.alpha = counter.toFloat() / homeViewModel.maxCount
        // TODO: return to normal opacity after %d seconds
    }
}