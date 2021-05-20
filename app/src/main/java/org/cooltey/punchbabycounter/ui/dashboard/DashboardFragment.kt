package org.cooltey.punchbabycounter.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.cooltey.punchbabycounter.MainActivity
import org.cooltey.punchbabycounter.R
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.databinding.FragmentDashboardBinding
import org.cooltey.punchbabycounter.databinding.FragmentHomeBinding
import org.cooltey.punchbabycounter.ui.home.HomeViewModel
import org.cooltey.punchbabycounter.utils.Prefs
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: DashboardViewModel
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    private var currentUserId = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardViewModel = DashboardViewModel(requireContext())
        currentUserId = Prefs.getCurrentId(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentUserId <= 0) {
            (requireActivity() as MainActivity).goToProfileTab()
        }

        dashboardViewModel.getSummaryByUserId(currentUserId).observe(viewLifecycleOwner, {
            binding.dashboardTotalCounts.text = getString(R.string.dashboard_total_counts, (it.level1_total + it.level2_total))
            binding.dashboardLevel1Counts.text = getString(R.string.dashboard_level_1_counts, it.level1_total)
            binding.dashboardLevel2Counts.text = getString(R.string.dashboard_level_2_counts, it.level2_total)
            binding.dashboardMostCountsDay.text = getString(R.string.dashboard_most_counts_day, dateFormat.format(it.date))
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}