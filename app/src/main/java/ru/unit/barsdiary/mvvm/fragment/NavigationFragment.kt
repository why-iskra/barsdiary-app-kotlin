package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentNavigationBinding
import ru.unit.barsdiary.mvvm.adapter.NavigationAdapter
import ru.unit.barsdiary.mvvm.viewmodel.GlobalViewModel
import ru.unit.barsdiary.other.function.configure

@AndroidEntryPoint
class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    private val globalModel: GlobalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNavigationBinding.bind(view)

        with(binding.viewPager) {
            configure()
            adapter = NavigationAdapter(this@NavigationFragment)
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
    }
}