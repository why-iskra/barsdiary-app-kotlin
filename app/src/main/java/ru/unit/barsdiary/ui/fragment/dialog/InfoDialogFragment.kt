package ru.unit.barsdiary.ui.fragment.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.DialogFragmentInfoBinding
import ru.unit.barsdiary.other.function.argumentDelegate


class InfoDialogFragment : DialogFragment() {

    companion object {
        private const val LABEL_KEY = "label"
        private const val DESCRIPTION_KEY = "description"

        fun config(label: String?, description: String?): Bundle {
            val bundle = Bundle()
            bundle.putString(LABEL_KEY, label)
            bundle.putString(DESCRIPTION_KEY, description)
            return bundle
        }
    }

    private val label: String? by argumentDelegate()
    private val description: String? by argumentDelegate()

    private var dismissListener: (() -> Unit) = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentInfoBinding.inflate(layoutInflater, null, false)

        binding.okButton.setOnClickListener {
            dismiss()
        }

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER
        binding.textViewLabel.text = label
        binding.textViewDescription.text = description

        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_Barsdiary_Dialog_Common)
        builder.setView(binding.root)
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissListener()
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        dismissListener()
        super.onCancel(dialog)
    }

    fun setDismissListener(dismissListener: () -> Unit) {
        this.dismissListener = dismissListener
    }
}