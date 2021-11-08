package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.mvvm.adapter.LessonsAdapter
import ru.unit.barsdiary.mvvm.fragment.bottomsheet.LessonBottomSheetDialogFragment
import ru.unit.barsdiary.mvvm.viewmodel.DiaryViewModel
import java.time.LocalDate

@AndroidEntryPoint
class LessonsFragment : BaseFragment(R.layout.fragment_lessons) {

    companion object {
        private const val LESSON_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG = "lessonBottomSheetDialogFragment"
    }

    private val model: DiaryViewModel by activityViewModels()

    private lateinit var lessonBottomSheetDialogFragment: LessonBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        lessonBottomSheetDialogFragment = parentFragmentManager
            .findFragmentByTag(LESSON_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG)
                as? LessonBottomSheetDialogFragment ?: LessonBottomSheetDialogFragment()

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (lessonBottomSheetDialogFragment.isAdded) {
            lessonBottomSheetDialogFragment.dismiss()
        }

        val epochDays = arguments?.getLong("epoch_days") ?: LocalDate.now().toEpochDay()
        val date: LocalDate = LocalDate.ofEpochDay(epochDays)

        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val infoTextView = view.findViewById<TextView>(R.id.infoTextView)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        model.lessonsLiveData.observeFreshly(viewLifecycleOwner) { data ->
            uiRefreshStop(view)
            val day = data[date]
            val adapter = day?.first?.lessons?.let {
                LessonsAdapter(it, day.second.homework) { lesson, homework ->
                    // TODO: move logic in viewmodel
                    if (!lessonBottomSheetDialogFragment.isAdded) {
                        var materials: String? = null
                        var homeworkWithInd: String? = null
                        if (homework != null) {
                            materials = model.materialsToString(homework.materials)
                            homeworkWithInd = model.homeworkToString(homework.homework ?: "", homework.individualHomeworks)
                        }

                        lessonBottomSheetDialogFragment.config(
                            lesson.discipline,
                            lesson.teacher,
                            lesson.office,
                            lesson.theme,
                            lesson.comment,
                            homeworkWithInd,
                            materials,
                            lesson.mark,
                            lesson.attendance
                        )
                        lessonBottomSheetDialogFragment.show(
                            parentFragmentManager,
                            LESSON_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG
                        )
                    }
                }
            }
            recyclerView.adapter = adapter

            if (adapter == null) {
                recyclerView.visibility = View.GONE
                infoTextView.visibility = View.VISIBLE

                if (day?.first?.isVacation == true) {
                    infoTextView.text = resources.getString(R.string.vacation)
                } else if (day?.first?.isHoliday == true) {
                    infoTextView.text = resources.getString(R.string.holiday)
                } else if (day?.first?.isWeekend == true) {
                    infoTextView.text = resources.getString(R.string.weekend)
                } else {
                    infoTextView.text = resources.getString(R.string.empty)
                }
            } else {
                recyclerView.visibility = View.VISIBLE
                infoTextView.visibility = View.GONE
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            if (it != null) {
                mainModel.handleException(it)
            }
        }

        model.updateFailIdLiveData.observe(viewLifecycleOwner) {
            if (model.updateFailIdLiveData.value?.contains(epochDays) == true) {
                uiRefreshStop(view)

                recyclerView.visibility = View.GONE
                infoTextView.visibility = View.VISIBLE
                infoTextView.text = resources.getString(R.string.error)
            }
        }

        uiRefreshStart(view)
        model.getLessons(epochDays, date)
    }

    override fun onStop() {
        val fragment = parentFragmentManager.findFragmentByTag(LESSON_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG)
        if (fragment != null) {
            parentFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }

        super.onStop()
    }

    // todo: add animations for update recyclerview
    private fun uiRefreshStart(view: View) {
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val infoTextView = view.findViewById<TextView>(R.id.infoTextView)

        infoTextView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        progressBarCircle.visibility = View.VISIBLE
    }

    private fun uiRefreshStop(view: View) {
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBar)

        progressBarCircle.visibility = View.GONE
    }

    fun config(epochDays: Long) {
        val args = Bundle()
        args.putLong("epoch_days", epochDays)
        arguments = args
    }
}