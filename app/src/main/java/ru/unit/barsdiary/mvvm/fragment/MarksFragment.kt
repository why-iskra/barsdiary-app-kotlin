package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentMarksBinding
import ru.unit.barsdiary.mvvm.adapter.MarksAdapter
import ru.unit.barsdiary.mvvm.fragment.bottomsheet.DisciplineMarksBottomSheetDialogFragment
import ru.unit.barsdiary.mvvm.viewmodel.StatisticsViewModel
import ru.unit.barsdiary.other.livedata.EventLiveData

@AndroidEntryPoint
class MarksFragment : BaseFragment(R.layout.fragment_marks) {

    companion object {
        private const val DISCIPLINE_MARKS_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG = "disciplineMarksBottomSheetDialogFragment"
    }

    private val model: StatisticsViewModel by activityViewModels()
    private lateinit var disciplineMarksBottomSheetDialogFragment: DisciplineMarksBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        disciplineMarksBottomSheetDialogFragment = parentFragmentManager
            .findFragmentByTag(DISCIPLINE_MARKS_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG)
                as? DisciplineMarksBottomSheetDialogFragment ?: DisciplineMarksBottomSheetDialogFragment()

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMarksBinding.bind(view)

        val recyclerView = binding.recyclerView
        with(recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
        }

        model.marksLiveData.observe(viewLifecycleOwner) {
            recyclerView.adapter = MarksAdapter(it) { position ->
                disciplineMarksBottomSheetDialogFragment.config(position)
                if (!disciplineMarksBottomSheetDialogFragment.isAdded) {
                    disciplineMarksBottomSheetDialogFragment.show(
                        parentFragmentManager, DISCIPLINE_MARKS_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG
                    )
                }
            }
        }

        binding.refreshButton.setOnClickListener {
            model.resetMarksOnly()
            model.getMarks()
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) { mainModel.handleException(it) }

        model.getMarks()
    }
}