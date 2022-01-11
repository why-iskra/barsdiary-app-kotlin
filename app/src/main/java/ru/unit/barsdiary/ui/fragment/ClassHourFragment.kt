package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentClassHourBinding
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class ClassHourFragment : BaseFragment(R.layout.fragment_class_hour) {

    private val model: GlobalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentClassHourBinding.bind(view)

        binding.refreshButton.setOnClickListener {
            model.resetClassHour()
            model.refreshClassHour()
        }

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        model.classHourLiveData.observe(viewLifecycleOwner) { classHour ->
            val date = model.classHourDateFormat(classHour.date) ?: classHour.date
            if (date.isNullOrBlank()) {
                binding.scrollView.isVisible = false
                binding.infoTextView.isVisible = true
            } else {
                binding.scrollView.isVisible = true
                binding.infoTextView.isVisible = false

                binding.dateTextView.text = date

                binding.themeTextView.isVisible = !classHour.theme.isNullOrBlank()
                binding.themeTextView.text = classHour.theme

                val begin = classHour.begin
                val end = classHour.end
                if (begin.isNullOrBlank() || end.isNullOrBlank()) {
                    binding.beginTextView.isVisible = false
                    binding.endTextView.isVisible = false
                } else {
                    binding.beginTextView.isVisible = true
                    binding.endTextView.isVisible = true

                    binding.beginTextView.text = begin
                    binding.endTextView.text = end
                }

                binding.placeTextView.isVisible = !classHour.place.isNullOrBlank()
                binding.placeTextView.text = classHour.place
            }
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

        model.refreshClassHour()
    }
}