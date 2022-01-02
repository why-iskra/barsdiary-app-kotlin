package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentMailBinding
import ru.unit.barsdiary.mvvm.adapter.MailAdapter
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import ru.unit.barsdiary.other.function.configure
import ru.unit.barsdiary.other.livedata.EventLiveData

@AndroidEntryPoint
class MailFragment : BaseFragment(R.layout.fragment_mail) {

    val model: GlobalViewModel by activityViewModels()

    private lateinit var binding: FragmentMailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMailBinding.bind(view)

        val mailAdapter = activity?.let { MailAdapter(it) }

        with(binding.viewPager) {
            configure()
            adapter = mailAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        binding.refreshButton.setOnClickListener {
            model.resetBoxes()
            model.refreshBoxes()
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

}