package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentSettingsBinding
import ru.unit.barsdiary.mvvm.viewmodel.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val model: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsBinding.bind(view)

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        model.settingsDataStore.syncWithSystemTheme.let {
            binding.syncThemeSwitch.isChecked = it
            binding.nightThemeSwitch.isEnabled = !it
        }
        binding.nightThemeSwitch.isChecked = model.settingsDataStore.nightTheme
        binding.fastAuthSwitch.isChecked = model.settingsDataStore.fastAuth

        binding.syncThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.syncWithSystemTheme = isChecked
        }

        binding.nightThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.nightTheme = isChecked
        }

        binding.fastAuthSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.fastAuth = isChecked
        }
    }
}