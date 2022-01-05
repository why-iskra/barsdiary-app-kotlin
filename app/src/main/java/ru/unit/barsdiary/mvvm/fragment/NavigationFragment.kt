package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentNavigationBinding
import ru.unit.barsdiary.mvvm.adapter.NavigationAdapter
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import ru.unit.barsdiary.other.function.configure
import ru.unit.barsdiary.other.function.findCurrentFragment

@AndroidEntryPoint
class NavigationFragment : BaseFragment(R.layout.fragment_navigation) {

    private val globalModel: GlobalViewModel by activityViewModels()

    private lateinit var binding: FragmentNavigationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNavigationBinding.bind(view)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            val controller =
                (binding.viewPager.findCurrentFragment(parentFragmentManager) as? NavigationGraphFragment)?.getNavController() ?: return@addCallback

            (binding.viewPager.adapter as NavigationAdapter).i

            val isNavigatedUp = controller.navigateUp()

            when {
                isNavigatedUp -> {
                    return@addCallback
                }
                binding.viewPager.currentItem != 0 -> {
                    binding.viewPager.currentItem = 0
                    binding.bottomNavBar.setActiveItem(0)
                }
                else -> {
                    activity?.finish()
                }
            }
        }

        with(binding.viewPager) {
            configure()
            isUserInputEnabled = false

            adapter = activity?.let { NavigationAdapter(it) }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position != binding.bottomNavBar.activeItemIndex && position < 4) {
                        binding.bottomNavBar.setActiveItem(position)
                    }
                }
            })
        }

        binding.bottomNavBar.setOnItemSelectedListener {
            binding.viewPager.currentItem = it
        }

        globalModel.notificationLiveData.observe(viewLifecycleOwner) {
            if (
                globalModel.birthsTodayLiveData.value == true
                || globalModel.hasInBoxLiveData.value == true
            ) {
                binding.bottomNavBar.setNotification(NavigationAdapter.FragmentId.Global.id, true)
            }
        }

        globalModel.refreshNotifications()

        globalModel.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            if (it != null) {
                mainModel.handleException(it)
            }
        }
    }
}