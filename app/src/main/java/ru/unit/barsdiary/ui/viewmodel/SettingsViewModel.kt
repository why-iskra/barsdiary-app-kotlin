package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val settingsDataStore: SettingsDataStore,
) : ViewModel()