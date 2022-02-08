package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.lib.HtmlUtils
import ru.unit.barsdiary.lib.function.argumentDelegate
import ru.unit.barsdiary.ui.adapter.LessonsAdapter
import ru.unit.barsdiary.ui.fragment.bottomsheet.LessonBottomSheetDialogFragment
import ru.unit.barsdiary.ui.viewmodel.DiaryViewModel
import java.time.LocalDate

@AndroidEntryPoint
class LessonsFragment : BaseFragment(R.layout.fragment_lessons) {

    companion object {
        private const val LESSON_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG = "lessonBottomSheetDialogFragment"

        private const val EPOCH_DAYS_KEY = "epochDays"

        fun config(epochDays: Long): Bundle {
            val bundle = Bundle()
            bundle.putLong(EPOCH_DAYS_KEY, epochDays)
            return bundle
        }
    }

    private val model: DiaryViewModel by activityViewModels()

    private val epochDays: Long by argumentDelegate()

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

        val date: LocalDate = LocalDate.ofEpochDay(epochDays)

//        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val infoTextView = view.findViewById<TextView>(R.id.infoTextView)
        val shimmerLayout = view.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)

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

                        var nextMaterials: String? = null
                        var nextHomeworkWithInd: String? = null
                        if (homework != null) {
                            nextMaterials = model.materialsToString(homework.nextMaterials)
                            nextHomeworkWithInd = model.homeworkToString(homework.nextHomework ?: "", homework.nextIndividualHomeworks)
                        }

                        val fullMaterials = buildString {
                            append(materials)

                            if(!nextMaterials.isNullOrBlank()) {
                                append(HtmlUtils.tagNewLine)
                                append(getString(R.string.for_next_lesson))
                                append(": ")
                                append(nextMaterials)
                            }
                        }

                        val fullHomework = buildString {
                            append(homeworkWithInd)

                            if(!nextHomeworkWithInd.isNullOrBlank()) {
                                append(HtmlUtils.tagNewLine)
                                append(getString(R.string.for_next_lesson))
                                append(": ")
                                append(nextHomeworkWithInd)
                            }
                        }

                        lessonBottomSheetDialogFragment.arguments = LessonBottomSheetDialogFragment.config(
                            lesson.discipline,
                            lesson.teacher,
                            lesson.office,
                            lesson.theme,
                            lesson.comment,
                            fullHomework,
                            fullMaterials,
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
                shimmerLayout.visibility = View.GONE
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
                shimmerLayout.visibility = View.GONE
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

                shimmerLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                infoTextView.visibility = View.GONE
//                infoTextView.text = resources.getString(R.string.error)
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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val infoTextView = view.findViewById<TextView>(R.id.infoTextView)

        infoTextView.visibility = View.GONE
        recyclerView.visibility = View.GONE
//        shimmerLayout.visibility = View.VISIBLE
    }

    private fun uiRefreshStop(view: View) {
        val shimmerLayout = view.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)
        shimmerLayout.visibility = View.GONE
    }
}