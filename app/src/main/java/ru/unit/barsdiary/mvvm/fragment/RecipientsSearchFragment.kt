package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentRecipientsSearchBinding
import ru.unit.barsdiary.mvvm.adapter.LoadStateAdapter
import ru.unit.barsdiary.mvvm.adapter.RecipientsSearchAdapter
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import ru.unit.barsdiary.mvvm.viewmodel.RecipientsSearchViewModel
import ru.unit.barsdiary.other.function.argumentDelegate
import javax.inject.Inject

@AndroidEntryPoint
class RecipientsSearchFragment : BaseFragment(R.layout.fragment_recipients_search) {

    private var searchText = ""

    private val type: Int by argumentDelegate()

    @Inject
    lateinit var assistedFactory: RecipientsSearchViewModel.AssistedFactory

    val model: RecipientsSearchViewModel by viewModels {
        RecipientsSearchViewModel.provideFactory(assistedFactory, type, searchText)
    }

    val globalModel: GlobalViewModel by activityViewModels()

    private lateinit var binding: FragmentRecipientsSearchBinding

    private val adapter = RecipientsSearchAdapter().apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipientsSearchBinding.bind(view)

        binding.textViewTitle.setText(when(type) {
            0 -> R.string.recipient_type_pupil
            1 -> R.string.recipient_type_employee
            2 -> R.string.recipient_type_parent
            else -> R.string.recipient_type_admin
        })

        adapter.clickableListener = {
            globalModel.addRecipient(it)
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.recyclerView.isVisible = false
                binding.infoTextView.isVisible = true
            } else {
                binding.recyclerView.isVisible = true
                binding.infoTextView.isVisible = false
            }
        }

        with(binding.recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
            if(adapter == null) {
                adapter = this@RecipientsSearchFragment.adapter.withLoadStateHeaderAndFooter(
                    LoadStateAdapter {
                        this@RecipientsSearchFragment.adapter.retry()
                    },
                    LoadStateAdapter {
                        this@RecipientsSearchFragment.adapter.retry()
                    }
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            model.searchResultFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    fun search() {
        adapter.refresh()
    }

    fun setSearch(text: String) {
        searchText = text
        runCatching {
            model.search = text
        }
    }
}