package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.observeFreshly
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentChildChoiceBinding
import ru.unit.barsdiary.lib.function.configure
import ru.unit.barsdiary.lib.function.endAnimatorListener
import ru.unit.barsdiary.lib.function.switchActivity
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.activity.MainActivity
import ru.unit.barsdiary.ui.adapter.ChildChoiceAdapter
import ru.unit.barsdiary.ui.viewmodel.ChangePupilViewModel

@AndroidEntryPoint
class ChildChoiceFragment : BaseFragment(R.layout.fragment_child_choice) {

    private val model: ChangePupilViewModel by viewModels()

    private lateinit var binding: FragmentChildChoiceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChildChoiceBinding.bind(view)

        val childChoiceAdapter = activity?.let { ChildChoiceAdapter(it, model.getPupils()) }

        with(binding.viewPager) {
            configure()
            adapter = childChoiceAdapter
            currentItem = model.getSelectedPupil()
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        binding.buttonChoose.setOnClickListener {
            model.changeChild(binding.viewPager.currentItem)
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> {
                    mainModel.hideNavigationBar()
                    uiHideButton()
                }
                EventLiveData.Event.LOADED -> {
                    uiShowButton()
                    activity?.switchActivity(context, MainActivity::class.java)
                }
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            if (it != null) {
                mainModel.handleException(it)
                mainModel.showNavigationBar()
            }
        }
    }

    private fun uiShowButton() {
        binding.buttonChoose.isClickable = true

        val fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)

        fadeOut.setAnimationListener(endAnimatorListener {
            binding.progressBarLine.visibility = FrameLayout.INVISIBLE
            binding.buttonChoose.startAnimation(fadeIn)
            binding.buttonChoose.visibility = FrameLayout.VISIBLE
        })
        binding.progressBarLine.startAnimation(fadeOut)
    }

    private fun uiHideButton() {
        binding.buttonChoose.isClickable = false

        val fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)

        fadeOut.setAnimationListener(endAnimatorListener {
            binding.buttonChoose.visibility = FrameLayout.INVISIBLE
            binding.progressBarLine.startAnimation(fadeIn)
            binding.progressBarLine.visibility = FrameLayout.VISIBLE
        })
        binding.buttonChoose.startAnimation(fadeOut)
    }
}