package ru.unit.barsdiary.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.unit.barsdiary.data.datastore.preferences.SafetyStringPreference
import ru.unit.barsdiary.data.utils.Safety
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val safety: Safety,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    val flow = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        val url = it[SERVER_URL]
        val login = it[LOGIN]
        val password = it[PASSWORD]
        val sessionId = it[SESSION_ID]

        Data(
            if (url == null) null else safety.decrypt(url),
            if (login == null) null else safety.decrypt(login),
            if (password == null) null else safety.decrypt(password),
            if (sessionId == null) null else safety.decrypt(sessionId),
        )
    }

    var serverUrl by SafetyStringPreference(context.dataStore, SERVER_URL, null, safety)
    var login by SafetyStringPreference(context.dataStore, LOGIN, null, safety)
    var password by SafetyStringPreference(context.dataStore, PASSWORD, null, safety)
    var sessionId by SafetyStringPreference(context.dataStore, SESSION_ID, null, safety)

    data class Data(
        val serverUrl: String?,
        val login: String?,
        val password: String?,
        val sessionId: String?,
    )

    companion object {
        private val SERVER_URL = stringPreferencesKey("serverUrl")
        private val LOGIN = stringPreferencesKey("login")
        private val PASSWORD = stringPreferencesKey("password")
        private val SESSION_ID = stringPreferencesKey("sessionId")
    }
}