package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        binding.tabMailLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_mailFragment)
        }

        binding.tabBirthdaysLayout.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_birthdaysFragment)
        }

        binding.tabMeetingsLayout.setOnClickListener {
            context?.let { Toast.makeText(it, getString(R.string.in_development), Toast.LENGTH_SHORT).show() }
        }

        binding.tabClassHoursLayout.setOnClickListener {
            context?.let { Toast.makeText(it, getString(R.string.in_development), Toast.LENGTH_SHORT).show() }
        }

        binding.tabEventsLayout.setOnClickListener {
            context?.let { Toast.makeText(it, getString(R.string.in_development), Toast.LENGTH_SHORT).show() }
        }

        binding.tabBirthdaysLayout.setOnClickListener {
            context?.let { Toast.makeText(it, getString(R.string.in_development), Toast.LENGTH_SHORT).show() }
        }

        binding.refreshButton.setOnClickListener {
            model.reset()
            model.refresh()
        }

        model.inBoxLiveData.observe(viewLifecycleOwner) {
            binding.tabMailCountView.count = it
        }

        model.birthdaysLiveData.observe(viewLifecycleOwner) {
            binding.tabBirthdaysCountView.count = it.birthdays.size
        }

        model.birthsTodayLiveData.observe(viewLifecycleOwner) {
            binding.tabBirthdaysCountView.accent = it
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