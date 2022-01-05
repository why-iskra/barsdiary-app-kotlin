package ru.unit.barsdiary.ui.fragment.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.databinding.BottomSheetDisciplineMarksBinding
import ru.unit.barsdiary.other.function.argumentDelegate
import ru.unit.barsdiary.ui.adapter.DisciplineMarksAdapter
import ru.unit.barsdiary.ui.viewmodel.StatisticsViewModel

@AndroidEntryPoint
class DisciplineMarksBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val POSITION_KEY = "position"

        fun config(position: Int): Bundle {
            val bundle = Bundle()
            bundle.putInt(POSITION_KEY, position)
            return bundle
        }
    }

    private val viewModel: StatisticsViewModel by activityViewModels()

    private val position: Int by argumentDelegate()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = BottomSheetDisciplineMarksBinding.inflate(
            inflater,
            container,
            false
        )

        with(binding.recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.marksLiveData.value?.get(position)?.let { discipline ->
            binding.textViewDiscipline.text = discipline.discipline
            binding.recyclerView.adapter = DisciplineMarksAdapter(discipline.marks)
        }

        return binding.root
    }
}