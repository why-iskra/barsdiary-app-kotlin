package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentGlobalBinding
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import ru.unit.barsdiary.other.livedata.EventLiveData
import ru.unit.barsdiary.other.questionAttribute

@AndroidEntryPoint
class GlobalFragment : BaseFragment(R.layout.fragment_global) {

    private val model: GlobalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGlobalBinding.bind(view)

        val normalColor = ContextCompat.getColor(requireContext(), requireContext().questionAttribute(android.R.attr.textColorSecondary).resourceId)
        val attentionColor = ContextCompat.getColor(requireContext(), R.color.amaranth)

        binding.textViewBirthdays.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_birthdaysFragment)
        }

        binding.textViewMail.setOnClickListener {
            findNavController().navigate(R.id.action_globalFragment_to_mailFragment)
        }

        binding.refreshButton.setOnClickListener {
            model.reset()
            model.refresh()
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.birthsTodayLiveData.observe(viewLifecycleOwner) {
            binding.textViewBirthdays.setTextColor(if (it) attentionColor else normalColor)
        }

        model.hasInBoxLiveData.observe(viewLifecycleOwner) {
            binding.textViewMail.setTextColor(if (it) attentionColor else normalColor)
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