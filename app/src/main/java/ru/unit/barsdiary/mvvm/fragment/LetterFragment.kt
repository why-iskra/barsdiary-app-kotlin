package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentLetterBinding
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.mvvm.viewmodel.LetterViewModel

@AndroidEntryPoint
class LetterFragment : Fragment(R.layout.fragment_letter) {

    companion object {
        private const val TITLE_KEY = "title"
        private const val FROM_KEY = "from"
        private const val DATE_KEY = "date"
        private const val TEXT_KEY = "text"
        private const val ID_KEY = "id"
        private const val IS_IN_BOX_KEY = "isInBox"
        private const val IS_NEW_KEY = "isNew"

        fun config(data: MessagePojo, isInBox: Boolean, text: String?): Bundle {
            val bundle = Bundle()
            bundle.putString(TITLE_KEY, data.subject)
            bundle.putString(FROM_KEY, if (isInBox) data.senderFullName else data.receivers.joinToString { it.fullname })
            bundle.putString(DATE_KEY, data.date)
            bundle.putString(TEXT_KEY, text)
            bundle.putInt(ID_KEY, data.id)
            bundle.putBoolean(IS_IN_BOX_KEY, isInBox)
            bundle.putBoolean(IS_NEW_KEY, data.isNew)

            return bundle
        }
    }

    private val model: LetterViewModel by viewModels()

    private lateinit var binding: FragmentLetterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLetterBinding.bind(view)

        binding.textViewSubject.text = arguments?.getString(TITLE_KEY)
        binding.textViewFrom.text = arguments?.getString(FROM_KEY)
        binding.textViewDate.text = arguments?.getString(DATE_KEY)

        with(binding.textViewText) {
            setLinkTextColor(requireContext().getColor(R.color.amaranth))
            movementMethod = LinkMovementMethod.getInstance()
            text = Html.fromHtml(arguments?.getString(TEXT_KEY), Html.FROM_HTML_MODE_COMPACT)
        }


        if (arguments?.getBoolean(IS_IN_BOX_KEY) == true && arguments?.getBoolean(IS_NEW_KEY) == true) {
            arguments?.getInt(ID_KEY)?.let {
                model.markRead(it)
            }
        }
    }
}