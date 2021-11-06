package ru.unit.barsdiary.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.unit.barsdiary.data.datastore.preferences.BooleanPreference
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val flow = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        Data(
            it[SYNC_WITH_SYSTEM_THEME],
            it[NIGHT_THEME],
            it[FAST_AUTH]
        )
    }

    var syncWithSystemTheme by BooleanPreference(context.dataStore, SYNC_WITH_SYSTEM_THEME, true)
    var nightTheme by BooleanPreference(context.dataStore, NIGHT_THEME, false)
    var fastAuth by BooleanPreference(context.dataStore, FAST_AUTH, false)

    data class Data(
        val syncWithSystemTheme: Boolean?,
        val nightTheme: Boolean?,
        val fastAuth: Boolean?,
    )

    companion object {
        private val SYNC_WITH_SYSTEM_THEME = booleanPreferencesKey("syncWithSystemTheme")
        private val NIGHT_THEME = booleanPreferencesKey("nightTheme")
        private val FAST_AUTH = booleanPreferencesKey("fastAuth")
    }
}