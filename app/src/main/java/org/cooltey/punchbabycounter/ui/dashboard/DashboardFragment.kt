package org.cooltey.punchbabycounter.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.cooltey.punchbabycounter.R
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.databinding.FragmentDashboardBinding
import org.cooltey.punchbabycounter.databinding.FragmentHomeBinding
import org.cooltey.punchbabycounter.ui.home.HomeViewModel
import org.cooltey.punchbabycounter.utils.Prefs

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: DashboardViewModel
    private var currentUserId = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardViewModel = DashboardViewModel(requireContext())
        currentUserId = Prefs.getCurrentId(requireActivity())
        return binding.root
    }
}