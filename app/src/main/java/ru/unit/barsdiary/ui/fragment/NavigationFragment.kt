package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentNavigationBinding
import ru.unit.barsdiary.other.function.configure
import ru.unit.barsdiary.ui.adapter.NavigationAdapter
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class NavigationFragment : BaseFragment(R.layout.fragment_navigation) {

    private val globalModel: GlobalViewModel by activityViewModels()

    private lateinit var binding: FragmentNavigationBinding

//    private var navBottomBarCurrentAnimator: ValueAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNavigationBinding.bind(view)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            if (activity?.supportFragmentManager?.backStackEntryCount == 0) {
                when {
                    binding.viewPager.currentItem != 0 -> {
                        binding.viewPager.currentItem = 0
                        binding.bottomNavBar.setActiveItem(0)
                    }
                    else -> {
                        activity?.finish()
                    }
                }
            } else {
                activity?.onBackPressed()
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

        mainModel.navigationBarShowLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.bottomNavBar.userInteractionEnabled = it
//                if (it) {
//                    binding.bottomNavBar.userInteractionEnabled = true
//
//                    navBottomBarCurrentAnimator?.cancel()
//                    navBottomBarCurrentAnimator = ValueAnimator.ofInt(
//                        binding.bottomNavBar.layoutParams.height,
//                        64.dp(requireContext())
//                    ).apply {
//                        duration = 1000
//                        addUpdateListener { ar ->
//                            binding.bottomNavBar.updateLayoutParams {
//                                height = ar.animatedValue as Int
//                            }
//                        }
//                        start()
//                    }
//                } else {
//                    binding.bottomNavBar.userInteractionEnabled = false
//
//                    navBottomBarCurrentAnimator?.cancel()
//                    navBottomBarCurrentAnimator = ValueAnimator.ofInt(
//                        binding.bottomNavBar.layoutParams.height,
//                        0.dp(requireContext())
//                    ).apply {
//                        duration = 1000
//                        addUpdateListener { ar ->
//                            binding.bottomNavBar.updateLayoutParams {
//                                height = ar.animatedValue as Int
//                            }
//                        }
//                        start()
//                    }
//                }
            } else {
                binding.bottomNavBar.userInteractionEnabled = true
            }
        }
    }
}