package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observeFreshly
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentDeveloperBinding
import ru.unit.barsdiary.mvvm.viewmodel.DeveloperViewModel

@AndroidEntryPoint
class DeveloperFragment : BaseFragment(R.layout.fragment_developer) {

    private val model: DeveloperViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDeveloperBinding.bind(view)

        binding.buttonFakeAuth.setOnClickListener {
            model.fakeAuth()
        }

        binding.buttonClearAll.setOnClickListener {
            model.clearAll()
        }

        binding.buttonCrash.setOnClickListener {
            model.crash()
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) { mainModel.handleException(it) }
    }
}