package ru.unit.barsdiary.mvvm.fragment.bottomsheet

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
import ru.unit.barsdiary.mvvm.adapter.DisciplineMarksAdapter
import ru.unit.barsdiary.mvvm.viewmodel.StatisticsViewModel

@AndroidEntryPoint
class DisciplineMarksBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: StatisticsViewModel by activityViewModels()

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

        arguments?.getInt("pos")?.let {
            viewModel.marksLiveData.value?.get(it)?.let { discipline ->
                binding.textViewDiscipline.text = discipline.discipline
                binding.recyclerView.adapter = DisciplineMarksAdapter(discipline.marks)
            }
        }

        return binding.root
    }

    fun config(pos: Int) {
        val args = Bundle()
        args.putInt("pos", pos)
        arguments = args
    }
}