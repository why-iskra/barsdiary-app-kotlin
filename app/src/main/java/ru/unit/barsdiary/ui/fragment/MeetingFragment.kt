package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentMeetingBinding
import ru.unit.barsdiary.lib.HtmlUtils
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class MeetingFragment : BaseFragment(R.layout.fragment_meeting) {

    private val model: GlobalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMeetingBinding.bind(view)

        binding.refreshButton.setOnClickListener {
            model.resetMeeting()
            model.refreshMeeting()
        }

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        model.meetingLiveData.observe(viewLifecycleOwner) { meeting ->
            val date = model.meetingDateFormat(meeting.date) ?: meeting.date
            if (date.isNullOrBlank()) {
                binding.scrollView.isVisible = false
                binding.infoTextView.isVisible = true
            } else {
                binding.scrollView.isVisible = true
                binding.infoTextView.isVisible = false

                binding.dateTextView.text = date

                binding.officeTextView.isVisible = !meeting.office.isNullOrBlank()
                binding.officeTextView.text = meeting.office

                val begin = meeting.begin
                val end = meeting.end
                if (begin.isNullOrBlank() || end.isNullOrBlank()) {
                    binding.beginTextView.isVisible = false
                    binding.endTextView.isVisible = false
                } else {
                    binding.beginTextView.isVisible = true
                    binding.endTextView.isVisible = true

                    binding.beginTextView.text = begin
                    binding.endTextView.text = end
                }

                val protocol = meeting.protocol
                with(binding.protocolTextView) {
                    isVisible = !protocol.isNullOrBlank()
                    setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.amaranth))
                    movementMethod = LinkMovementMethod.getInstance()

                    protocol?.let {
                        text = HtmlUtils.convert(
                            model.document(
                                getString(R.string.download_protocol),
                                it
                            )
                        )
                    }
                }

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

        model.refreshMeeting()
    }
}