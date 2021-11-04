package ru.unit.barsdiary.mvvm.fragment.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.BottomSheetLessonBinding


class LessonBottomSheetDialogFragment : BottomSheetDialogFragment() {

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

        val args = arguments
        if (args != null) {
            args.getString("discipline").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewDiscipline.visibility = View.GONE
                } else {
                    binding.textViewDiscipline.visibility = View.VISIBLE
                    binding.textViewDiscipline.text = it
                }
            }
            args.getString("teacher").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewTeacher.visibility = View.GONE
                } else {
                    binding.textViewTeacher.visibility = View.VISIBLE
                    binding.textViewTeacher.text = it
                }
            }
            args.getString("office").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewOffice.visibility = View.GONE
                } else {
                    binding.textViewOffice.visibility = View.VISIBLE
                    binding.textViewOffice.text = it
                }
            }
            args.getString("theme").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewTheme.visibility = View.GONE
                } else {
                    binding.textViewTheme.visibility = View.VISIBLE
                    binding.textViewTheme.text = it
                }
            }
            args.getString("comment").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewComment.visibility = View.GONE
                } else {
                    binding.textViewComment.visibility = View.VISIBLE
                    binding.textViewComment.text = it
                }
            }
            args.getString("homework").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewHomework.visibility = View.GONE
                } else {
                    binding.textViewHomework.visibility = View.VISIBLE

                    binding.textViewHomework.setLinkTextColor(requireContext().getColor(R.color.amaranth))
                    binding.textViewHomework.movementMethod = LinkMovementMethod.getInstance()
                    binding.textViewHomework.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
                }
            }
            args.getString("materials").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewMaterials.visibility = View.GONE
                } else {
                    binding.textViewMaterials.visibility = View.VISIBLE

                    binding.textViewMaterials.setLinkTextColor(requireContext().getColor(R.color.amaranth))
                    binding.textViewMaterials.movementMethod = LinkMovementMethod.getInstance()
                    binding.textViewMaterials.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
                }
            }
            args.getString("mark").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewMark.visibility = View.GONE
                } else {
                    binding.textViewMark.visibility = View.VISIBLE
                    binding.textViewMark.text = it
                }
            }
            args.getString("attendance").let {
                if (it == null || it.isEmpty()) {
                    binding.textViewAttendance.visibility = View.GONE
                } else {
                    binding.textViewAttendance.visibility = View.VISIBLE
                    binding.textViewAttendance.text = it
                }
            }
        } else {
            binding.textViewDiscipline.visibility = View.GONE
            binding.textViewTeacher.visibility = View.GONE
            binding.textViewOffice.visibility = View.GONE
            binding.textViewTheme.visibility = View.GONE
            binding.textViewComment.visibility = View.GONE
            binding.textViewHomework.visibility = View.GONE
            binding.textViewMaterials.visibility = View.GONE
            binding.textViewMark.visibility = View.GONE
            binding.textViewAttendance.visibility = View.GONE
        }

        return binding.root
    }

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
    ) {
        val args = Bundle()
        args.putString("discipline", discipline)
        args.putString("teacher", teacher)
        args.putString("office", office)
        args.putString("theme", theme)
        args.putString("comment", comment)
        args.putString("homework", homework)
        args.putString("materials", materials)
        args.putString("mark", mark)
        args.putString("attendance", attendance)
        arguments = args
    }
}