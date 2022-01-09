package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentLogsBinding
import ru.unit.barsdiary.ui.viewmodel.DeveloperViewModel
import ru.unit.barsdiary.lib.HtmlUtils

@AndroidEntryPoint
class LogsFragment : BaseFragment(R.layout.fragment_logs) {

    private val model: DeveloperViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLogsBinding.bind(view)

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER
        binding.horizontalScrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.debugView.text = HtmlUtils.convert(model.getLog().joinToString(HtmlUtils.tagNewLine)) // fixme: concurrent list

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            model.updateLogFlow.collectLatest {
                binding.debugView.text = HtmlUtils.convert(model.getLog().joinToString(HtmlUtils.tagNewLine))
            }
        }
    }
}