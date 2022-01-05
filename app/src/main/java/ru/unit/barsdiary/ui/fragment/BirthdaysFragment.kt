package ru.unit.barsdiary.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentBirthdaysBinding
import ru.unit.barsdiary.other.livedata.EventLiveData
import ru.unit.barsdiary.ui.adapter.BirthdaysAdapter
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class BirthdaysFragment : BaseFragment(R.layout.fragment_birthdays) {

    private val model: GlobalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBirthdaysBinding.bind(view)

        val recyclerView = binding.recyclerView
        with(recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.refreshButton.setOnClickListener {
            model.resetBirthdays()
            model.refreshBirthdays()
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.birthdaysLiveData.observe(viewLifecycleOwner) {
            if(it.birthdays.isEmpty()) {
                binding.infoTextView.isVisible = true
                binding.recyclerView.isVisible = false
            } else {
                binding.infoTextView.isVisible = false
                binding.recyclerView.isVisible = true

                binding.recyclerView.adapter = BirthdaysAdapter(it.birthdays) { date ->
                    model.birthdayDateFormat(date)
                }
            }
        }

        model.birthsTodayLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.konfettiView.build().run {
                    addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    setDirection(0.0, 359.0)
                    setSpeed(1f, 5f)
                    setFadeOutEnabled(true)
                    setTimeToLive(2000L)
                    addShapes(Shape.Square, Shape.Circle)
                    addSizes(Size(12), Size(16, 6f))
                    setPosition(-50f, binding.konfettiView.width + 50f, -50f, -50f)
                    streamFor(200, 1000L)
                }
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        model.refreshBirthdays()
    }
}