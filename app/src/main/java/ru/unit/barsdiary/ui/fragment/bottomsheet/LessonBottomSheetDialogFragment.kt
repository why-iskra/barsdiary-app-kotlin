package ru.unit.barsdiary.ui.fragment.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.BottomSheetLessonBinding
import ru.unit.barsdiary.lib.HtmlUtils
import ru.unit.barsdiary.lib.function.argumentDelegate


class LessonBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {

        private const val DISCIPLINE_KEY = "discipline"
        private const val TEACHER_KEY = "teacher"
        private const val OFFICE_KEY = "office"
        private const val THEME_KEY = "theme"
        private const val COMMENT_KEY = "comment"
        private const val HOMEWORK_KEY = "homework"
        private const val MATERIALS_KEY = "materials"
        private const val MARK_KEY = "mark"
        private const val ATTENDANCE_KEY = "attendance"

        fun config(
            discipline: String?,
            teacher: String?,
            office: String?,
            theme: String?,
            comment: String?,
            homework: String?,
            materials: String?,
            mark: String?,
            attendance: String?,
        ): Bundle {
            val bundle = Bundle()
            bundle.putString(DISCIPLINE_KEY, discipline)
            bundle.putString(TEACHER_KEY, teacher)
            bundle.putString(OFFICE_KEY, office)
            bundle.putString(THEME_KEY, theme)
            bundle.putString(COMMENT_KEY, comment)
            bundle.putString(HOMEWORK_KEY, homework)
            bundle.putString(MATERIALS_KEY, materials)
            bundle.putString(MARK_KEY, mark)
            bundle.putString(ATTENDANCE_KEY, attendance)
            return bundle
        }
    }

    private val discipline: String? by argumentDelegate()
    private val teacher: String? by argumentDelegate()
    private val office: String? by argumentDelegate()
    private val theme: String? by argumentDelegate()
    private val comment: String? by argumentDelegate()
    private val homework: String? by argumentDelegate()
    private val materials: String? by argumentDelegate()
    private val mark: String? by argumentDelegate()
    private val attendance: String? by argumentDelegate()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = BottomSheetLessonBinding.inflate(inflater, container, false)

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        if (discipline.isNullOrBlank()) {
            binding.textViewDiscipline.visibility = View.GONE
        } else {
            binding.textViewDiscipline.visibility = View.VISIBLE
            binding.textViewDiscipline.text = discipline
        }

        if (teacher.isNullOrBlank()) {
            binding.textViewTeacher.visibility = View.GONE
        } else {
            binding.textViewTeacher.visibility = View.VISIBLE
            binding.textViewTeacher.text = teacher
        }

        if (office.isNullOrBlank()) {
            binding.textViewOffice.visibility = View.GONE
        } else {
            binding.textViewOffice.visibility = View.VISIBLE
            binding.textViewOffice.text = office
        }

        if (theme.isNullOrBlank()) {
            binding.textViewTheme.visibility = View.GONE
        } else {
            binding.textViewTheme.visibility = View.VISIBLE
            binding.textViewTheme.text = HtmlUtils.convert(theme)
        }

        if (comment.isNullOrBlank()) {
            binding.textViewComment.visibility = View.GONE
        } else {
            binding.textViewComment.visibility = View.VISIBLE
            binding.textViewComment.text = HtmlUtils.convert(comment)
        }

        if (homework.isNullOrBlank()) {
            binding.textViewHomework.visibility = View.GONE
        } else {
            binding.textViewHomework.visibility = View.VISIBLE

            binding.textViewHomework.setLinkTextColor(requireContext().getColor(R.color.amaranth))
            binding.textViewHomework.movementMethod = LinkMovementMethod.getInstance()
            binding.textViewHomework.text = HtmlUtils.convert(homework)
        }

        if (materials.isNullOrBlank()) {
            binding.textViewMaterials.visibility = View.GONE
        } else {
            binding.textViewMaterials.visibility = View.VISIBLE

            binding.textViewMaterials.setLinkTextColor(requireContext().getColor(R.color.amaranth))
            binding.textViewMaterials.movementMethod = LinkMovementMethod.getInstance()
            binding.textViewMaterials.text = HtmlUtils.convert(materials)
        }

        if (mark.isNullOrBlank()) {
            binding.textViewMark.visibility = View.GONE
        } else {
            binding.textViewMark.visibility = View.VISIBLE
            binding.textViewMark.text = mark
        }

        if (attendance.isNullOrBlank()) {
            binding.textViewAttendance.visibility = View.GONE
        } else {
            binding.textViewAttendance.visibility = View.VISIBLE
            binding.textViewAttendance.text = attendance
        }

        return binding.root
    }
}