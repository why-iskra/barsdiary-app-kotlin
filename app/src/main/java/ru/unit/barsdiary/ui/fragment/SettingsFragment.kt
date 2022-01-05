package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentSettingsBinding
import ru.unit.barsdiary.ui.viewmodel.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val model: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsBinding.bind(view)

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER


        model.settingsDataStore.developerMode.let {
            binding.devModeThemeSwitch.visibility = if (it) View.VISIBLE else View.GONE
            binding.devModeThemeSwitch.isChecked = it
        }

        model.settingsDataStore.syncWithSystemTheme.let {
            binding.syncThemeSwitch.isChecked = it
            binding.nightThemeSwitch.isEnabled = !it
        }

        binding.nightThemeSwitch.isChecked = model.settingsDataStore.nightTheme
        binding.fastAuthSwitch.isChecked = model.settingsDataStore.fastAuth
        binding.errorDialogsSwitch.isChecked = model.settingsDataStore.errorDialogs

        binding.timeoutEditText.setText(model.settingsDataStore.clientTimeout.toString())

        binding.syncThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.syncWithSystemTheme = isChecked
            binding.nightThemeSwitch.isEnabled = !isChecked
        }

        binding.nightThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.nightTheme = isChecked
        }

        binding.fastAuthSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.fastAuth = isChecked
        }

        binding.devModeThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.developerMode = isChecked
        }

        binding.errorDialogsSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.errorDialogs = isChecked
        }

        binding.timeoutEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                runCatching {
                    val timeout = s.toString().toInt()
                    model.settingsDataStore.clientTimeout = if (timeout < 10) 10 else timeout
                }.onFailure {
                    model.settingsDataStore.clientTimeout = 60
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}