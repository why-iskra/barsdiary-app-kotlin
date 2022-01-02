package ru.unit.barsdiary.data.datastore.preferences

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ru.unit.barsdiary.data.utils.Safety
import java.io.IOException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class OldSafetyStringPreference(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<String>,
    private val defaultValue: String?,
    private val safety: Safety,
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return runBlocking {
            val result = dataStore.data.catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.first()[key]

            return@runBlocking if (result.isNullOrEmpty()) {
                null
            } else {
                safety.decrypt(result)
            }
        } ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        runBlocking {
            dataStore.edit {
                it[key] = if (value == null) "" else safety.encrypt(value)
            }
        }
    }
}