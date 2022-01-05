package ru.unit.barsdiary.ui.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.DialogFragmentServerListBinding
import ru.unit.barsdiary.ui.adapter.ServerListAdapter
import ru.unit.barsdiary.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class ServerListDialogFragment : DialogFragment() {

    private val model: AuthViewModel by activityViewModels()

    private var clickListener: (String) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentServerListBinding.inflate(layoutInflater, null, false)

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            model.serverListLiveData.value?.let {
                adapter = ServerListAdapter(it) { url ->
                    clickListener(url)
                    this@ServerListDialogFragment.dismiss()
                }
            }
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_Barsdiary_Dialog_Common)
        builder.setView(binding.root)
        return builder.create()
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        clickListener = listener
    }
}