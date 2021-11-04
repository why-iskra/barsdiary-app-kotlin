package ru.unit.barsdiary.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val PREFERENCES_NAME = "barsdiary"

        const val AUTH_KEY = "auth"
        const val SELECTED_PUPIL_KEY = "selected_pupil"
    }

    fun get(): SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    fun edit(): SharedPreferences.Editor = get().edit()
}