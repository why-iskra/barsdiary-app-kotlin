package ru.unit.barsdiary.mvvm.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.DialogFragmentPickDateBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


class PickDateDialogFragment : DialogFragment() {

    private var onSelectListener: ((LocalDate) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding =
            DialogFragmentPickDateBinding.inflate(layoutInflater, null, false)

        val date = arguments?.let {
            LocalDate.of(
                it.getInt("year"),
                it.getInt("month"),
                it.getInt("day")
            )
        } ?: LocalDate.now()

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.calendarView.date = date.atTime(LocalTime.NOON).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            onSelectListener?.invoke(LocalDate.of(year, month + 1, dayOfMonth))
            dismiss()
        }

        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_Barsdiary_Dialog_Common)
        builder.setView(binding.root)
        return builder.create()
    }

    fun send(date: LocalDate) {
        val args = Bundle()
        args.putInt("day", date.dayOfMonth)
        args.putInt("month", date.monthValue)
        args.putInt("year", date.year)
        arguments = args
    }

    fun setOnSelectListener(onSelectListener: ((LocalDate) -> Unit)?) {
        this.onSelectListener = onSelectListener
    }

}