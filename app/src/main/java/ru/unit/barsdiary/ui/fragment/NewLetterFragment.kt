package ru.unit.barsdiary.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observeFreshly
import androidx.navigation.fragment.findNavController
import com.google.android.material.internal.TextWatcherAdapter
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentNewLetterBinding
import ru.unit.barsdiary.service.SendingMessageService
import ru.unit.barsdiary.ui.viewmodel.NewLetterViewModel


@AndroidEntryPoint
class NewLetterFragment : BaseFragment(R.layout.fragment_new_letter) {

    private val model: NewLetterViewModel by activityViewModels()

    private lateinit var binding: FragmentNewLetterBinding

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted) {
            openFilePicker()
        }
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val path = result.data?.data ?: return@registerForActivityResult

            val name = context?.let {
                DocumentFile.fromSingleUri(it, path)?.name
            }

            model.file(name, path)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.prepare()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewLetterBinding.bind(view)

        binding.textViewTo.setOnClickListener {
            findNavController().navigate(R.id.action_newLetterFragment_to_recipientsFragment)
        }

        binding.textViewAttach.setOnClickListener {
            checkPermission()
        }

        binding.sendButton.setOnClickListener {
            binding.sendButton.isEnabled = false
            findNavController().navigateUp()

            val subject = binding.editTextSubject.text?.toString() ?: return@setOnClickListener
            val text = binding.editText.text?.toString() ?: return@setOnClickListener
            val file = model.attachmentLiveData.value

            activity?.run {
                SendingMessageService.start(
                    this,
                    model.getRecipients(),
                    subject,
                    text,
                    file?.first,
                    file?.second
                )
            }
        }

        binding.editText.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkAvailableSending()
            }
        })

        binding.editTextSubject.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkAvailableSending()
            }
        })

        model.recipientsLiveData.observe(viewLifecycleOwner) {
            checkAvailableSending()

            if (it.isEmpty()) {
                binding.textViewTo.setText(R.string.button_to_text)
            } else {
                binding.textViewTo.text = it.joinToString(separator = ", ") { user -> user.fullName.toString() }
            }
        }

        model.attachmentLiveData.observe(viewLifecycleOwner) { file ->
            if(file == null) {
                binding.textViewAttach.setText(R.string.button_attach_text)
            } else {
                binding.textViewAttach.text = file.first
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            if (it != null) {
                mainModel.handleException(it)
            }
        }

        checkAvailableSending()
    }

    private fun checkAvailableSending() {
        binding.sendButton.isEnabled = !binding.editText.text.isNullOrBlank()
                && !binding.editTextSubject.text.isNullOrBlank()
                && model.getRecipients().isNotEmpty()
    }

    private fun checkPermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun openFilePicker() {
        val intent = Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }, getString(R.string.choose_file))
        filePickerLauncher.launch(intent)
    }
}