package org.cooltey.punchbabycounter.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.cooltey.punchbabycounter.MainActivity
import org.cooltey.punchbabycounter.R
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.databinding.FragmentDashboardBinding
import org.cooltey.punchbabycounter.databinding.ViewDashboardItemBinding
import org.cooltey.punchbabycounter.utils.GeneralUtil
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
            binding.dashboardTotalCounts.text = getString(R.string.dashboard_total_counts, GeneralUtil.getFormattedNumber(it.level1_total + it.level2_total))
            binding.dashboardLevel1Counts.text = getString(R.string.dashboard_level_1_counts, GeneralUtil.getFormattedNumber(it.level1_total))
            binding.dashboardLevel2Counts.text = getString(R.string.dashboard_level_2_counts, GeneralUtil.getFormattedNumber(it.level2_total))
            binding.dashboardMostCountsDay.text = getString(R.string.dashboard_most_counts_day, dateFormat.format(it.date))
        })

        dashboardViewModel.getListByUserId(currentUserId).observe(viewLifecycleOwner, {
            binding.dashboardRecycler.setHasFixedSize(true)
            binding.dashboardRecycler.adapter = RecyclerViewAdapter(it)
            binding.dashboardRecycler.layoutManager = LinearLayoutManager(requireActivity())
        })
    }

    private inner class RecyclerViewAdapter(private val list: List<Record>) : RecyclerView.Adapter<ListItemHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
            return ListItemHolder(ViewDashboardItemBinding.inflate(LayoutInflater.from(requireContext()), parent, false))
        }

        override fun onBindViewHolder(holder: ListItemHolder, pos: Int) {
            holder.bindItem(list[pos])
        }
    }

    private inner class ListItemHolder constructor(private val binding: ViewDashboardItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(record: Record) {
            binding.dashboardDate.text = dateFormat.format(record.date)
            binding.dashboardLevel1Counts.text = getString(R.string.dashboard_level_1_counts, GeneralUtil.getFormattedNumber(record.level1))
            binding.dashboardLevel2Counts.text = getString(R.string.dashboard_level_2_counts, GeneralUtil.getFormattedNumber(record.level2))
            record.note?.let {
                binding.dashboardNote.text = getString(R.string.dashboard_note, it)
            } ?: run {
                binding.dashboardNote.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.dashboardRecycler.adapter = null
        _binding = null
    }
}