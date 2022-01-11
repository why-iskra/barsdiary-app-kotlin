package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentGlobalBinding
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class GlobalFragment : BaseFragment(R.layout.fragment_global) {

    private val model: GlobalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGlobalBinding.bind(view)

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.tabMailLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_mailFragment)
        }

        binding.tabBirthdaysLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_birthdaysFragment)
        }

        binding.tabMeetingsLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_meetingFragment)
        }

        binding.tabClassHoursLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_classHourFragment)
        }

        binding.tabEventsLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_eventsFragment)
        }

        binding.tabAdvertBoardLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_advertBoardFragment)
        }

        binding.refreshButton.setOnClickListener {
            model.reset()
            model.refresh()
        }

        model.inBoxLiveData.observe(viewLifecycleOwner) {
            binding.tabMailCountView.count = it
        }

        model.advertBoardLiveData.observe(viewLifecycleOwner) {
            binding.tabAdvertBoardCountView.count = it.items.size
        }

        model.birthdaysLiveData.observe(viewLifecycleOwner) {
            binding.tabBirthdaysCountView.count = it.birthdays.size
        }

        model.birthsTodayLiveData.observe(viewLifecycleOwner) {
            binding.tabBirthdaysCountView.accent = it
        }

        model.meetingLiveData.observe(viewLifecycleOwner) {
            val date = it.date
            if (date.isNullOrBlank()) {
                binding.tabMeetingsCountView.count = 0
            } else {
                binding.tabMeetingsCountView.count = 1
                binding.tabMeetingsCountView.accent = model.webIsToday(date)
            }
        }

        model.classHourLiveData.observe(viewLifecycleOwner) {
            val date = it.date
            if (date.isNullOrBlank()) {
                binding.tabClassHoursCountView.count = 0
            } else {
                binding.tabClassHoursCountView.count = 1
                binding.tabClassHoursCountView.accent = model.webIsToday(date)
            }
        }

        model.eventsLiveData.observe(viewLifecycleOwner) { raw ->
            val events = raw.items.filter {
                !it.theme.isNullOrBlank() || !it.date.isNullOrBlank() || !it.dateStr.isNullOrBlank()
            }

            binding.tabEventsCountView.count = events.size
            binding.tabEventsCountView.accent = events.find {
                val date = it.date
                return@find if (date.isNullOrBlank()) {
                    false
                } else {
                    model.webIsToday(date)
                }
            } != null
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
    }

    override fun onResume() {
        super.onResume()
        model.refresh()
    }
}