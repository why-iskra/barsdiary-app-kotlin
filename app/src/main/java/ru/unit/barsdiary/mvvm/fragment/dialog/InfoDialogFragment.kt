package ru.unit.barsdiary.mvvm.fragment.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.DialogFragmentInfoBinding


class InfoDialogFragment : DialogFragment() {

    private var dismissListener: (() -> Unit) = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentInfoBinding.inflate(layoutInflater, null, false)

        binding.okButton.setOnClickListener {
            dismiss()
        }

        val args = arguments

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER
        binding.textViewLabel.text = args?.getString("label")
        binding.textViewDescription.text = args?.getString("description")

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

    fun send(label: String, description: String) {
        val args = Bundle()
        args.putString("label", label)
        args.putString("description", description)

        arguments = args
    }

    fun setDismissListener(dismissListener: () -> Unit) {
        this.dismissListener = dismissListener
    }
}