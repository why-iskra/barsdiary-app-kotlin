package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentChildChoiceBinding
import ru.unit.barsdiary.mvvm.activity.MainActivity
import ru.unit.barsdiary.mvvm.adapter.ChildChoiceAdapter
import ru.unit.barsdiary.mvvm.viewmodel.ChangePupilViewModel
import ru.unit.barsdiary.other.endAnimatorListener
import ru.unit.barsdiary.other.function.configure
import ru.unit.barsdiary.other.function.switchActivity
import ru.unit.barsdiary.other.livedata.EventLiveData

@AndroidEntryPoint
class ChildChoiceFragment : Fragment(R.layout.fragment_child_choice) {

    private val model: ChangePupilViewModel by viewModels()

    private lateinit var binding: FragmentChildChoiceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChildChoiceBinding.bind(view)

        with(binding.viewPager) {
            configure()
            adapter = ChildChoiceAdapter(this@ChildChoiceFragment, model.getPupils())
            currentItem = model.getSelectedPupil()
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        binding.buttonChoose.setOnClickListener {
            model.changeChild(binding.viewPager.currentItem)
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> uiHideButton()
                EventLiveData.Event.LOADED -> {
                    uiShowButton()
                    activity?.switchActivity(context, MainActivity::class.java)
                }
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