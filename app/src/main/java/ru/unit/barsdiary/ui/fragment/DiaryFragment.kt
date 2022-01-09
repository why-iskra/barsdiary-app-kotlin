package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentDiaryBinding
import ru.unit.barsdiary.lib.function.configure
import ru.unit.barsdiary.lib.function.inflateFactory
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.adapter.DiaryAdapter
import ru.unit.barsdiary.ui.fragment.dialog.PickDateDialogFragment
import ru.unit.barsdiary.ui.viewmodel.DiaryViewModel
import java.time.LocalDate

@AndroidEntryPoint
class DiaryFragment : BaseFragment(R.layout.fragment_diary) {

    companion object {
        private const val PICK_DATE_DIALOG_TAG = "pickDateDialog"
    }

    private val model: DiaryViewModel by activityViewModels()
    private lateinit var binding: FragmentDiaryBinding

    private lateinit var pickDateDialog: PickDateDialogFragment

    private lateinit var onPageChangeListener: OnPageChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        pickDateDialog = parentFragmentManager
            .findFragmentByTag(PICK_DATE_DIALOG_TAG) as? PickDateDialogFragment ?: PickDateDialogFragment()

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDiaryBinding.bind(view)
        with(binding) {
            lifecycleOwner = this@DiaryFragment
            model = this@DiaryFragment.model
        }

        pickDateDialog.setOnSelectListener {
            model.dateLiveData.postValue(it)
            setupViewPager(it)
        }

        binding.textSwitcherDate.inflateFactory(layoutInflater, R.layout.text_switcher_diary_date)

        binding.calendarButtonView.setOnClickListener {
            if (!pickDateDialog.isAdded) {
                pickDateDialog.arguments = PickDateDialogFragment.config(model.dateLiveData.value ?: LocalDate.now())
                pickDateDialog.show(parentFragmentManager, PICK_DATE_DIALOG_TAG)
            }
        }

        binding.refreshButton.setOnClickListener {
            model.reset()
            setupViewPager(model.dateLiveData.value)
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.dateLiveData.observe(viewLifecycleOwner) {
            model.dateStringLiveData.postValue(it.format(DiaryViewModel.dateFormat))
        }

        with(binding.viewPagerLessons) {
            configure()
            onPageChangeListener = OnPageChangeListener()
            registerOnPageChangeCallback(onPageChangeListener)
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        setupViewPager(model.dateLiveData.value)
    }

    private fun setupViewPager(date: LocalDate?) {
        date ?: return

        val transaction = parentFragmentManager.beginTransaction()
        parentFragmentManager.fragments.forEach {
            if (it is LessonsFragment) {
                transaction.remove(it)
            }
        }
        transaction.commitAllowingStateLoss()

        with(binding.viewPagerLessons) {
            unregisterOnPageChangeCallback(onPageChangeListener)

            adapter = this@DiaryFragment.activity?.let { DiaryAdapter(it, date) }
            setCurrentItem(500, false)

            onPageChangeListener = OnPageChangeListener()
            registerOnPageChangeCallback(onPageChangeListener)
        }
    }

    inner class OnPageChangeListener : ViewPager2.OnPageChangeCallback() {
        private var pos: Int = 500

        override fun onPageSelected(position: Int) {
            if (position > pos) {
                model.dateLiveData.postValue(model.dateLiveData.value?.plusDays(1))
            } else if (position < pos) {
                model.dateLiveData.postValue(model.dateLiveData.value?.minusDays(1))
            }

            pos = position
            super.onPageSelected(position)
        }
    }
}