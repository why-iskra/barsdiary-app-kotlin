package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentRecipientsBinding
import ru.unit.barsdiary.lib.function.configure
import ru.unit.barsdiary.ui.adapter.RecipientsAdapter
import ru.unit.barsdiary.ui.adapter.RecipientsTypeAdapter
import ru.unit.barsdiary.ui.viewmodel.NewLetterViewModel

@AndroidEntryPoint
class RecipientsFragment : BaseFragment(R.layout.fragment_recipients) {

    private val model: NewLetterViewModel by activityViewModels()

    private lateinit var binding: FragmentRecipientsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipientsBinding.bind(view)

        val recipientsTypeAdapter = activity?.let { RecipientsTypeAdapter(it) }

        with(binding.viewPager) {
            configure()
            adapter = recipientsTypeAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()


        with(binding.recyclerViewRecipients) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.searchButtonView.setOnClickListener {
            model.searchTriggerLiveData.post()
        }

        binding.editTextSearch.setText(model.searchTextLiveData.value)

        binding.editTextSearch.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable) {
                model.searchTextLiveData.postValue(s.toString())
            }
        })

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                model.searchTriggerLiveData.post()
                true
            } else {
                false
            }
        }

        binding.doneButton.setOnClickListener {
            findNavController().navigateUp()
        }

        model.recipientsLiveData.observe(viewLifecycleOwner) {
            binding.recyclerViewRecipients.adapter = RecipientsAdapter(it) { user ->
                model.removeRecipient(user)
            }
        }
    }
}