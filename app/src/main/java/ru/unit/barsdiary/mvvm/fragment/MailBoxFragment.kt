package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentMailBoxBinding
import ru.unit.barsdiary.mvvm.adapter.BoxAdapter
import ru.unit.barsdiary.mvvm.adapter.LoadStateAdapter
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class MailBoxFragment : Fragment(R.layout.fragment_mail_box) {

    private val globalModel: GlobalViewModel by activityViewModels()
    private lateinit var binding: FragmentMailBoxBinding

    private var inBox: Boolean = true

    @Inject
    lateinit var adapter: BoxAdapter

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

        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            LoadStateAdapter {
                adapter.retry()
            },
            LoadStateAdapter {
                adapter.retry()
            }
        )

        globalModel.resetBoxAdapterLiveData.observe(viewLifecycleOwner) {
            adapter.refresh()
        }
    }

    fun isInBox(value: Boolean) {
        inBox = value
    }

    fun setTitle(@StringRes res: Int) {
        binding.textViewTitle.setText(res)
    }
}