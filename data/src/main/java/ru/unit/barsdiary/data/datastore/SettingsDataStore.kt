package ru.unit.barsdiary.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.unit.barsdiary.data.datastore.preferences.BooleanPreference
import ru.unit.barsdiary.data.datastore.preferences.IntPreference
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
            it[FAST_AUTH],
            it[DEVELOPER_MODE],
            it[CLIENT_TIMEOUT],
            it[ERROR_DIALOGS],
            it[ENABLE_CHUCKER],
            it[ENABLE_CRASHLYTICS],
        )
    }

    var syncWithSystemTheme by BooleanPreference(context.dataStore, SYNC_WITH_SYSTEM_THEME, true)
    var nightTheme by BooleanPreference(context.dataStore, NIGHT_THEME, false)
    var fastAuth by BooleanPreference(context.dataStore, FAST_AUTH, false)
    var developerMode by BooleanPreference(context.dataStore, DEVELOPER_MODE, false)
    var clientTimeout by IntPreference(context.dataStore, CLIENT_TIMEOUT, 60)
    var errorDialogs by BooleanPreference(context.dataStore, ERROR_DIALOGS, true)
    var enableChucker by BooleanPreference(context.dataStore, ENABLE_CHUCKER, false)
    var enableCrashlytics by BooleanPreference(context.dataStore, ENABLE_CRASHLYTICS, false)

    data class Data(
        val syncWithSystemTheme: Boolean?,
        val nightTheme: Boolean?,
        val fastAuth: Boolean?,
        val developerMode: Boolean?,
        val clientTimeout: Int?,
        val errorDialogs: Boolean?,
        val enableChucker: Boolean?,
        val enableCrashlytics: Boolean?,
    )

    companion object {
        private val SYNC_WITH_SYSTEM_THEME = booleanPreferencesKey("syncWithSystemTheme")
        private val NIGHT_THEME = booleanPreferencesKey("nightTheme")
        private val FAST_AUTH = booleanPreferencesKey("fastAuth")
        private val DEVELOPER_MODE = booleanPreferencesKey("developerMode")
        private val CLIENT_TIMEOUT = intPreferencesKey("clientTimeout")
        private val ERROR_DIALOGS = booleanPreferencesKey("errorDialogs")
        private val ENABLE_CHUCKER = booleanPreferencesKey("enableChucker")
        private val ENABLE_CRASHLYTICS = booleanPreferencesKey("enableCrashlytics")
    }
}