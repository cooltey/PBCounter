package org.cooltey.punchbabycounter.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.cooltey.punchbabycounter.MainActivity
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.databinding.FragmentHomeBinding
import org.cooltey.punchbabycounter.ui.profile.ProfileViewModel
import org.cooltey.punchbabycounter.utils.GeneralUtil
import org.cooltey.punchbabycounter.utils.Prefs
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

        // TODO: navigate to profile page if userId = -1
        homeViewModel.getRecordByUserId(currentUserId).observe(viewLifecycleOwner, {
            recordData = it
        })

        binding.touchCircleLeft.setOnClickListener {
            homeViewModel.leftCounterIncrement()
            homeViewModel.leftCounter.observe(viewLifecycleOwner, {
                gradientCircleUpdate(it, binding.touchCircleLeft)
                vibratePhone(200)
            })
        }

        binding.touchCircleRight.setOnClickListener {
            homeViewModel.rightCounterIncrement()
            homeViewModel.rightCounter.observe(viewLifecycleOwner, {
                gradientCircleUpdate(it, binding.touchCircleRight)
                vibratePhone(400)
            })
        }
    }

    override fun onPause() {
        super.onPause()
        save()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun save() {
        if (currentUserId <= 0) {
            return
        }
        homeViewModel.leftCounter.observe(viewLifecycleOwner, { leftCounter ->
            homeViewModel.rightCounter.observe(viewLifecycleOwner, { rightCounter ->
                homeViewModel.save(buildRecordData(leftCounter, rightCounter)) {
                    // TODO: show note dialog?
                    Toast.makeText(requireContext(), "Counter updated", Toast.LENGTH_SHORT).show()
                }
            })
        })
    }

    // TODO: have flexible levels
    private fun buildRecordData(level1: Long = 0, level2: Long = 0): Record {
        return recordData?.let {
            Record(uid = it.uid,
                userId = currentUserId,
                level1 = level1 + it.level1,
                level2 = level2 + it.level2,
                date = it.date,
                note = it.note)
        } ?: run {
            Record(userId = currentUserId,
                level1 = level1,
                level2 = level2,
                date = GeneralUtil.getToday())
        }
    }

    private fun vibratePhone(vibrateDuration: Long) {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrateDuration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(vibrateDuration)
        }
    }

    private fun gradientCircleUpdate(counter: Long, view: View) {
        view.alpha = counter.toFloat() / homeViewModel.maxCount
        // TODO: return to normal opacity after %d seconds
    }
}