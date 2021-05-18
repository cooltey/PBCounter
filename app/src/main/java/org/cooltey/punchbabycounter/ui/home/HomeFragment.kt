package org.cooltey.punchbabycounter.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.cooltey.punchbabycounter.databinding.FragmentHomeBinding
import org.cooltey.punchbabycounter.utils.Prefs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private var currentUserId = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        currentUserId = Prefs.getCurrentId(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.touchCircleLeft.setOnClickListener {
            homeViewModel.leftCounterIncrement()
            homeViewModel.leftCounter.observe(viewLifecycleOwner, {
                gradientCircleUpdate(it, binding.touchCircleLeft)
            })
            vibratePhone(200)
        }

        binding.touchCircleRight.setOnClickListener {
            homeViewModel.rightCounterIncrement()
            homeViewModel.rightCounter.observe(viewLifecycleOwner, {
                gradientCircleUpdate(it, binding.touchCircleRight)
            })
            vibratePhone(400)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun vibratePhone(vibrateDuration: Long) {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrateDuration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(vibrateDuration)
        }
    }

    private fun gradientCircleUpdate(counter: Int, view: View) {
        view.alpha = counter.toFloat() / homeViewModel.maxCount
        // TODO: return to normal opacity after %d seconds
    }
}