package org.cooltey.punchbabycounter.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.cooltey.punchbabycounter.MainActivity
import org.cooltey.punchbabycounter.R
import org.cooltey.punchbabycounter.database.Record
import org.cooltey.punchbabycounter.database.Summary
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
    private val dateYearFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    private val dateQueryFormat = SimpleDateFormat("yyyy-MM-01", Locale.getDefault())
    private val dateTextFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private var currentUserId = -1L
    private var showByMode = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardViewModel = DashboardViewModel(requireContext())
        currentUserId = Prefs.getCurrentId(requireActivity())
        showByMode = Prefs.showBy(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentUserId <= 0) {
            (requireActivity() as MainActivity).goToProfileTab()
        }

        if (showByMode == 0) {
            binding.dashboardShowByDay.isChecked = true
        } else {
            binding.dashboardShowByMonth.isChecked = true
        }
        dashboardViewModel.updateOrderBy(showByMode)
        binding.dashboardShowByGroup.setOnCheckedChangeListener { _, i ->
            val newOrderBy = if (i == R.id.dashboardShowByDay) 0 else 1
            Prefs.showBy(requireActivity(), newOrderBy)
            dashboardViewModel.updateOrderBy(newOrderBy)
            binding.dashboardRecycler.adapter?.notifyDataSetChanged()
        }

        dashboardViewModel.orderBy.observe(viewLifecycleOwner, {
            dashboardViewModel.getList(currentUserId).removeObservers(this)
            observeList()
            binding.dashboardRecycler.adapter?.notifyDataSetChanged()
        })

        dashboardViewModel.getUserInfo(currentUserId).observe(viewLifecycleOwner, {
            it?.let {
                binding.dashboardSummary.text = getString(R.string.dashboard_summary_title)
            }
        })

        dashboardViewModel.getSummaryByUserId(currentUserId).observe(viewLifecycleOwner, {
            it?.let {
                binding.dashboardTotalCounts.text = getString(R.string.dashboard_total_counts, GeneralUtil.getFormattedNumber(it.level1Total + it.level2Total))
                binding.dashboardLevel1Counts.text = getString(R.string.dashboard_level_1_counts, GeneralUtil.getFormattedNumber(it.level1Total))
                binding.dashboardLevel2Counts.text = getString(R.string.dashboard_level_2_counts, GeneralUtil.getFormattedNumber(it.level2Total))
                binding.dashboardMostCountsDay.text = getString(R.string.dashboard_most_counts_day, dateFormat.format(it.summaryDate))
            }
        })

        dashboardViewModel.getMonthlySummaryByUserId(currentUserId, dateQueryFormat.format(Date())).observe(viewLifecycleOwner, {
            it?.let {
                binding.dashboardMonth.text = dateTextFormat.format(Date())
                binding.dashboardMonthlyTotalCounts.text = getString(R.string.dashboard_total_counts, GeneralUtil.getFormattedNumber(it.level1Total + it.level2Total))
                binding.dashboardMonthlyLevel1Counts.text = getString(R.string.dashboard_level_1_counts, GeneralUtil.getFormattedNumber(it.level1Total))
                binding.dashboardMonthlyLevel2Counts.text = getString(R.string.dashboard_level_2_counts, GeneralUtil.getFormattedNumber(it.level2Total))
                binding.dashboardMonthlyMostCountsDay.text = getString(R.string.dashboard_most_counts_day, dateFormat.format(it.summaryDate))
            }
        })

        binding.dashboardRecycler.setHasFixedSize(true)
        binding.dashboardRecycler.layoutManager = LinearLayoutManager(requireActivity())
        observeList()
    }

    private fun observeList() {
        dashboardViewModel.getList(currentUserId).observe(viewLifecycleOwner, {
            it?.let {
                binding.dashboardRecycler.adapter = RecyclerViewAdapter(it)
            }
        })
    }

    private inner class RecyclerViewAdapter(private val list: List<Any>) : RecyclerView.Adapter<ListItemHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
            return ListItemHolder(ViewDashboardItemBinding.inflate(LayoutInflater.from(requireContext()), parent, false))
        }

        override fun onBindViewHolder(holder: ListItemHolder, pos: Int) {
            if (list[pos] is Record) {
                holder.bindItem(list[pos] as Record)
            } else if (list[pos] is Summary) {
                holder.bindItem(list[pos] as Summary)
            }
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

        fun bindItem(summary: Summary) {
            binding.dashboardDate.text = dateYearFormat.format(summary.summaryDate)
            binding.dashboardLevel1Counts.text = getString(R.string.dashboard_level_1_counts, GeneralUtil.getFormattedNumber(summary.level1Total))
            binding.dashboardLevel2Counts.text = getString(R.string.dashboard_level_2_counts, GeneralUtil.getFormattedNumber(summary.level2Total))
            binding.dashboardNote.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.dashboardRecycler.adapter = null
        _binding = null
    }
}