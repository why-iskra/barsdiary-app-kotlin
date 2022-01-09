package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observeFreshly
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentDeveloperBinding
import ru.unit.barsdiary.lib.function.configure
import ru.unit.barsdiary.ui.adapter.DeveloperAdapter
import ru.unit.barsdiary.ui.viewmodel.DeveloperViewModel

@AndroidEntryPoint
class DeveloperFragment : BaseFragment(R.layout.fragment_developer) {

    private val model: DeveloperViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDeveloperBinding.bind(view)

        val developerAdapter = activity?.let { DeveloperAdapter(it) }

        with(binding.viewPager) {
            configure()
            adapter = developerAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            if (it != null) {
                mainModel.handleException(it)
            }
        }
    }
}