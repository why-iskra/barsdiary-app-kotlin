package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentAdvertBoardBinding
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.adapter.AdvertBoardAdapter
import ru.unit.barsdiary.ui.fragment.bottomsheet.AdvertBoardItemBottomSheetDialogFragment
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class AdvertBoardFragment : BaseFragment(R.layout.fragment_advert_board) {

    companion object {
        private const val ADVERT_BOARD_ITEM_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG = "advertBoardItemBottomSheetDialogFragment"
    }

    private val model: GlobalViewModel by activityViewModels()

    private lateinit var advertBoardItemBottomSheetDialogFragment: AdvertBoardItemBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        advertBoardItemBottomSheetDialogFragment = parentFragmentManager
            .findFragmentByTag(ADVERT_BOARD_ITEM_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG)
            as? AdvertBoardItemBottomSheetDialogFragment ?: AdvertBoardItemBottomSheetDialogFragment()

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAdvertBoardBinding.bind(view)

        if (advertBoardItemBottomSheetDialogFragment.isAdded) {
            advertBoardItemBottomSheetDialogFragment.dismiss()
        }

        val recyclerView = binding.recyclerView
        with(recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.refreshButton.setOnClickListener {
            model.resetAdvertBoard()
            model.refreshAdvertBoard()
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.advertBoardLiveData.observe(viewLifecycleOwner) {
            if (it.items.isEmpty()) {
                binding.infoTextView.isVisible = true
                binding.recyclerView.isVisible = false
            } else {
                binding.infoTextView.isVisible = false
                binding.recyclerView.isVisible = true

                binding.recyclerView.adapter = AdvertBoardAdapter(it.items, { date ->
                    model.advertBoardDateFormat(date)
                }) {
                    with(advertBoardItemBottomSheetDialogFragment) {
                        if (!isAdded) {
                            arguments = AdvertBoardItemBottomSheetDialogFragment.config(
                                it.theme,
                                it.school,
                                it.author,
                                it.advertDate,
                                it.fileUrl,
                                it.message
                            )

                            show(
                                this@AdvertBoardFragment.parentFragmentManager,
                                ADVERT_BOARD_ITEM_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG
                            )
                        }
                    }
                }
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        model.refreshAdvertBoard()
    }
}