package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentFinalMarksBinding
import ru.unit.barsdiary.mvvm.adapter.FinalMarksAdapter
import ru.unit.barsdiary.mvvm.viewmodel.PersonViewModel
import ru.unit.barsdiary.other.livedata.EventLiveData

@AndroidEntryPoint
class FinalMarksFragment : Fragment(R.layout.fragment_final_marks) {

    private val model: PersonViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFinalMarksBinding.bind(view)

        with(binding.recyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.refreshButton.setOnClickListener {
            model.resetTotalMarks()
            model.refreshTotalMarks()
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.totalMarksLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            val context = context ?: return@observe

            val adapter = ArrayAdapter(context, R.layout.spinner_item, model.subperiodNames(it)).apply {
                setDropDownViewResource(R.layout.spinner_dropdown_item)
            }

            binding.spinnerView.adapter = adapter
        }

        binding.spinnerView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val totalMarks = model.totalMarksLiveData.value ?: return
                val code = totalMarks.subperiods[position].code
                val filtered = model.filteredDisciplineMarks(totalMarks, code)

                binding.recyclerView.adapter = FinalMarksAdapter(filtered)
                binding.recyclerView.scheduleLayoutAnimation()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        model.refreshTotalMarks()
    }
}