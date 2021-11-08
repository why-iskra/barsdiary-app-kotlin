package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentStatisticsBinding
import ru.unit.barsdiary.mvvm.viewmodel.StatisticsViewModel
import ru.unit.barsdiary.other.inflateFactory
import ru.unit.barsdiary.other.livedata.EventLiveData

@AndroidEntryPoint
class StatisticsFragment : BaseFragment(R.layout.fragment_statistics) {

    private val model: StatisticsViewModel by activityViewModels()
    private lateinit var binding: FragmentStatisticsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticsBinding.bind(view)
        with(binding) {
            lifecycleOwner = this@StatisticsFragment
            model = this@StatisticsFragment.model
        }

        model.initialize(
            getString(R.string.was_absent),
            getString(R.string.days_until_vacation),
            getString(R.string.average_grade)
        )

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.textSwitcherAttendance.inflateFactory(layoutInflater, R.layout.text_switcher_statistics_attendance)
        binding.textSwitcherDaysUntilVacation.inflateFactory(layoutInflater, R.layout.text_switcher_statistics_days_until_vacation)
        binding.textSwitcherAverageGrade.inflateFactory(layoutInflater, R.layout.text_switcher_statistics_average_grade)

        binding.refreshButton.setOnClickListener {
            model.reset()
            model.refresh()
        }

        binding.buttonMarks.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_marksFragment)
        }

        model.progressChartLiveData.observe(viewLifecycleOwner) {
            binding.chart.update(it.first, it.second, true) {}
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        model.refresh()
    }
}