package ru.unit.barsdiary.mvvm.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity(@LayoutRes val res: Int) : AppCompatActivity(res) {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            settingsDataStore.flow.collectLatest { data ->
                if (data.syncWithSystemTheme == null || data.syncWithSystemTheme == true) {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else if (data.nightTheme == true) {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                }
            }
        }
    }

}