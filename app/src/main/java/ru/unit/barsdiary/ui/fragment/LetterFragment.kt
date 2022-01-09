package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observeFreshly
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentLetterBinding
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.lib.HtmlUtils
import ru.unit.barsdiary.lib.function.argumentDelegate
import ru.unit.barsdiary.ui.viewmodel.GlobalViewModel

@AndroidEntryPoint
class LetterFragment : BaseFragment(R.layout.fragment_letter) {

    companion object {
        private const val TITLE_KEY = "title"
        private const val FROM_KEY = "from"
        private const val DATE_KEY = "date"
        private const val LETTER_TEXT_KEY = "letterText"
        private const val LETTER_ID_KEY = "letterId"
        private const val IS_IN_BOX_KEY = "isInBox"
        private const val IS_NEW_KEY = "isNew"

        fun config(data: MessagePojo, isInBox: Boolean, text: String?): Bundle {
            val bundle = Bundle()
            bundle.putString(TITLE_KEY, data.subject)
            bundle.putString(FROM_KEY, if (isInBox) data.senderFullName else data.receivers.joinToString { it.fullname })
            bundle.putString(DATE_KEY, data.date)
            bundle.putString(LETTER_TEXT_KEY, text)
            bundle.putInt(LETTER_ID_KEY, data.id)
            bundle.putBoolean(IS_IN_BOX_KEY, isInBox)
            bundle.putBoolean(IS_NEW_KEY, data.isNew)

            return bundle
        }
    }

    private val model: GlobalViewModel by activityViewModels()

    private val title: String? by argumentDelegate()
    private val from: String? by argumentDelegate()
    private val date: String? by argumentDelegate()
    private val letterText: String? by argumentDelegate()
    private val letterId: Int by argumentDelegate()
    private val isInBox: Boolean by argumentDelegate()
    private val isNew: Boolean by argumentDelegate()

    private lateinit var binding: FragmentLetterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLetterBinding.bind(view)

        binding.textViewSubject.text = title
        binding.textViewFrom.text = from
        binding.textViewDate.text = date

        with(binding.textViewText) {
            setLinkTextColor(requireContext().getColor(R.color.amaranth))
            movementMethod = LinkMovementMethod.getInstance()
            text = HtmlUtils.convert(letterText)
        }

        if (isInBox && isNew) {
            model.markRead(letterId)
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            if (it != null) {
                mainModel.handleException(it)
            }
        }
    }
}