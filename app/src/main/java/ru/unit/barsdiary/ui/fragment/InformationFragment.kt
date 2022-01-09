package ru.unit.barsdiary.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.*
import ru.unit.barsdiary.domain.person.pojo.ClassmatePojo
import ru.unit.barsdiary.domain.person.pojo.EmployeePojo
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.viewmodel.AccountViewModel

@AndroidEntryPoint
class InformationFragment : BaseFragment(R.layout.fragment_information) {

    private val model: AccountViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentInformationBinding.bind(view)
        with(binding) {
            lifecycleOwner = this@InformationFragment
            model = this@InformationFragment.model
        }

        binding.refreshButton.setOnClickListener {
            model.resetProfile()
            model.refreshProfile()
        }

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.classShowHideButtonView.setOnShowHideListener {
            binding.linearLayoutClassmates.visibility = if (it) View.GONE else View.VISIBLE
        }

        binding.employeesShowHideButtonView.setOnShowHideListener {
            binding.linearLayoutEmployees.visibility = if (it) View.GONE else View.VISIBLE
        }

        binding.classShowHideButtonView.setShow(true)
        binding.linearLayoutClassmates.visibility = View.GONE
        binding.employeesShowHideButtonView.setShow(true)
        binding.linearLayoutEmployees.visibility = View.GONE

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.employeesLiveData.observe(viewLifecycleOwner) { list ->
            binding.linearLayoutEmployees.removeAllViews()

            list.forEachIndexed { i: Int, data: EmployeePojo ->
                val item = LinearItemEmployeeBinding.inflate(LayoutInflater.from(context), binding.linearLayoutEmployees, false)

                item.textViewName.text = "${i + 1}. ${data.name}"
                item.textViewJob.text = data.employerJobs.joinToString(", ")

                binding.linearLayoutEmployees.addView(item.root)
            }
        }

        model.classmatesLiveData.observe(viewLifecycleOwner) { list ->
            binding.linearLayoutClassmates.removeAllViews()

            list.forEachIndexed { i: Int, data: ClassmatePojo ->
                val item = LinearItemClassmateBinding.inflate(LayoutInflater.from(context), binding.linearLayoutClassmates, false)
                item.textView.text = "${i + 1}. ${data.name}"

                binding.linearLayoutClassmates.addView(item.root)
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        model.refreshProfile()
    }
}