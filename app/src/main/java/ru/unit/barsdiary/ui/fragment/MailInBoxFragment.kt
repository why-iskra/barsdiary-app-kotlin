package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.R
import ru.unit.barsdiary.ui.viewmodel.InBoxViewModel

class MailInBoxFragment : MailBoxFragment() {

    private val model: InBoxViewModel by viewModels()

    init {
        isInBox(true)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(R.string.in_box)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            model.inBoxFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}