package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentMailBinding
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.mvvm.adapter.MailAdapter
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import ru.unit.barsdiary.other.function.configure
import ru.unit.barsdiary.other.livedata.EventLiveData

@AndroidEntryPoint
class MailFragment : Fragment(R.layout.fragment_mail) {

    val model: GlobalViewModel by activityViewModels()

    private lateinit var binding: FragmentMailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMailBinding.bind(view)

        with(binding.viewPager) {
            configure()
            adapter = MailAdapter(this@MailFragment)
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
    }

    fun openLetterFragment(data: MessagePojo, isInBox: Boolean) {
        var text: String? = data.text
        runCatching {
            text = buildString {
                append(data.text)
                data.attachments.map {
                    model.document(it.originalName, it.downloadLink)
                }.forEach {
                    append("<br/>")
                    append("<br/>")
                    append(it)
                }
            }
        }

        findNavController().navigate(R.id.action_mailFragment_to_letterFragment, LetterFragment.config(data, isInBox, text))
    }
}