package ru.unit.barsdiary.data.datastore.preferences

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class IntPreference(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<Int>,
    private val defaultValue: Int,
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return runBlocking {
            dataStore.data.catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.first()[key]
        } ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        runBlocking {
            dataStore.edit {
                it[key] = value
            }
        }
    }
}