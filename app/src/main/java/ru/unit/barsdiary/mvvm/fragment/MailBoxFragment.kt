package ru.unit.barsdiary.mvvm.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentMailBoxBinding
import ru.unit.barsdiary.mvvm.adapter.BoxAdapter
import ru.unit.barsdiary.mvvm.adapter.LoadStateAdapter
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class MailBoxFragment : BaseFragment(R.layout.fragment_mail_box) {

    private val globalModel: GlobalViewModel by activityViewModels()
    private lateinit var binding: FragmentMailBoxBinding

    private var inBox: Boolean = true

    @Inject
    lateinit var adapter: BoxAdapter

    var selected: List<Int> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMailBoxBinding.bind(view)

        with(binding.recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
        }

        adapter.isInBox = inBox
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        adapter.setMailOnClick {
            (parentFragment as? MailFragment)?.openLetterFragment(it, inBox)
        }

        binding.imageButtonViewDelete.setOnClickListener {
            if (inBox) {
                globalModel.deleteInBox(selected)
            } else {
                globalModel.deleteOutBox(selected)
            }
        }

        binding.imageButtonViewSelectAll.setOnClickListener {
            adapter.selectAll()
        }

        binding.imageButtonViewClear.setOnClickListener {
            adapter.resetSelected()
        }

        binding.imageButtonViewDelete.setOnClickListener {
            if (inBox) {
                globalModel.deleteInBox(selected)
            } else {
                globalModel.deleteOutBox(selected)
            }
        }

        toolbarVisibility(adapter.hasSelected())

        adapter.setOnSelect {
            toolbarVisibility(it.isNotEmpty())
            selected = it
        }

        if(binding.recyclerView.adapter == null) {
            binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                LoadStateAdapter {
                    adapter.retry()
                },
                LoadStateAdapter {
                    adapter.retry()
                }
            )
        }

        globalModel.resetBoxAdapterLiveData.observe(viewLifecycleOwner) {
            adapter.resetSelected()
            adapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            adapter.loadStateFlow.collectLatest {
                val error = when {
                    it.append is LoadState.Error -> it.append
                    it.prepend is LoadState.Error -> it.prepend
                    it.refresh is LoadState.Error -> it.refresh
                    else -> return@collectLatest
                }

                if (error is LoadState.Error) {
                    mainModel.handleException(error.error)
                }
            }
        }
    }

    private fun toolbarVisibility(hasSelected: Boolean) {
        (if (hasSelected) View.VISIBLE else View.GONE).let {
            binding.imageButtonViewDelete.visibility = it
            binding.imageButtonViewSelectAll.visibility = it
            binding.imageButtonViewClear.visibility = it
        }
    }

    fun isInBox(value: Boolean) {
        inBox = value
    }

    fun setTitle(@StringRes res: Int) {
        binding.textViewTitle.setText(res)
    }
}