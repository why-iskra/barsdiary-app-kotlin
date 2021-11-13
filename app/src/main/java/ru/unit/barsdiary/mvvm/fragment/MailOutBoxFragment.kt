package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.R
import ru.unit.barsdiary.mvvm.viewmodel.OutBoxViewModel

class MailOutBoxFragment : MailBoxFragment() {

    private val model: OutBoxViewModel by viewModels()

    init {
        isInBox(false)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(R.string.out_box)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            model.outBoxFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}