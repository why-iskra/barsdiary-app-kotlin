package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentAdvertBoardBinding
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.adapter.EventsAdapter
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class EventsFragment : BaseFragment(R.layout.fragment_events) {

    private val model: GlobalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAdvertBoardBinding.bind(view)

        with(binding.recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.refreshButton.setOnClickListener {
            model.resetEvents()
            model.refreshEvents()
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.eventsLiveData.observe(viewLifecycleOwner) {
            if (it.items.isEmpty()) {
                binding.infoTextView.isVisible = true
                binding.recyclerView.isVisible = false
            } else {
                binding.infoTextView.isVisible = false
                binding.recyclerView.isVisible = true

                binding.recyclerView.adapter = EventsAdapter(it.items) { date ->
                    model.eventDateFormat(date)
                }
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        model.refreshEvents()
    }
}