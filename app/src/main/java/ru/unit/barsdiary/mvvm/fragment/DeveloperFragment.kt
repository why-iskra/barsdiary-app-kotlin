package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observeFreshly
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentDeveloperBinding
import ru.unit.barsdiary.mvvm.adapter.DeveloperAdapter
import ru.unit.barsdiary.mvvm.viewmodel.DeveloperViewModel
import ru.unit.barsdiary.other.function.configure

@AndroidEntryPoint
class DeveloperFragment : BaseFragment(R.layout.fragment_developer) {

    private val model: DeveloperViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDeveloperBinding.bind(view)

        with(binding.viewPager) {
            configure()
            adapter = DeveloperAdapter(this@DeveloperFragment)
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            if(it != null) {
                mainModel.handleException(it)
            }
        }
    }
}