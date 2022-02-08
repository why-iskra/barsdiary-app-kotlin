package ru.unit.barsdiary.ui.fragment.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.BottomSheetAdvertBoardItemBinding
import ru.unit.barsdiary.lib.HtmlUtils
import ru.unit.barsdiary.lib.function.argumentDelegate
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel


class AdvertBoardItemBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {

        private const val THEME_KEY = "theme"
        private const val SCHOOL_KEY = "school"
        private const val AUTHOR_KEY = "author"
        private const val DATE_KEY = "date"
        private const val FILE_KEY = "file"
        private const val MESSAGE_KEY = "message"

        fun config(
            theme: String?,
            school: String?,
            author: String?,
            date: String?,
            file: String?,
            message: String?,
        ): Bundle {
            val bundle = Bundle()
            bundle.putString(THEME_KEY, theme)
            bundle.putString(SCHOOL_KEY, school)
            bundle.putString(AUTHOR_KEY, author)
            bundle.putString(DATE_KEY, date)
            bundle.putString(FILE_KEY, file)
            bundle.putString(MESSAGE_KEY, message)
            return bundle
        }
    }

    private val theme: String? by argumentDelegate()
    private val school: String? by argumentDelegate()
    private val date: String? by argumentDelegate()
    private val author: String? by argumentDelegate()
    private val file: String? by argumentDelegate()
    private val message: String? by argumentDelegate()

    private val model: GlobalViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = BottomSheetAdvertBoardItemBinding.inflate(inflater, container, false)

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.layoutAuthor.isVisible = !author.isNullOrBlank()
        binding.textViewAuthor.text = author

        binding.layoutTheme.isVisible = !theme.isNullOrBlank()
        binding.textViewTheme.text = theme

        val parsedDate = model.advertBoardDateFormat(date)
        binding.layoutDate.isVisible = !parsedDate.isNullOrBlank()
        binding.textViewDate.text = parsedDate

        binding.layoutSchool.isVisible = !school.isNullOrBlank()
        binding.textViewSchool.text = school

        binding.layoutFile.isVisible = !file.isNullOrBlank()
        with(binding.textViewFile) {
            setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.amaranth))
            movementMethod = LinkMovementMethod.getInstance()

            file?.let {
                text = HtmlUtils.convert(model.document(getString(R.string.download_file), it))
            }
        }

        binding.layoutMessage.isVisible = !message.isNullOrBlank()
        with(binding.textViewMessage) {
            setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.amaranth))
            movementMethod = LinkMovementMethod.getInstance()
            text = HtmlUtils.convert(message)
        }

        return binding.root
    }
}