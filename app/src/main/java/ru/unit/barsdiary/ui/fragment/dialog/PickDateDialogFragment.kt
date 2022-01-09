package ru.unit.barsdiary.ui.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.DialogFragmentPickDateBinding
import ru.unit.barsdiary.lib.function.argumentDelegate
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


class PickDateDialogFragment : DialogFragment() {

    companion object {
        private const val YEAR_KEY = "year"
        private const val MONTH_KEY = "month"
        private const val DAY_KEY = "day"

        fun config(date: LocalDate): Bundle {
            val bundle = Bundle()
            bundle.putInt(YEAR_KEY, date.year)
            bundle.putInt(MONTH_KEY, date.monthValue)
            bundle.putInt(DAY_KEY, date.dayOfMonth)
            return bundle
        }
    }

    private val year: Int by argumentDelegate()
    private val month: Int by argumentDelegate()
    private val day: Int by argumentDelegate()

    private var onSelectListener: ((LocalDate) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding =
            DialogFragmentPickDateBinding.inflate(layoutInflater, null, false)

        val date = LocalDate.of(year, month, day)

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

    fun setOnSelectListener(onSelectListener: ((LocalDate) -> Unit)?) {
        this.onSelectListener = onSelectListener
    }

}