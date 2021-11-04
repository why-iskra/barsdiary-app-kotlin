package ru.unit.barsdiary.data.sharedpreferences

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSharedPreferences @Inject constructor(
    private val preferences: SharedPreferencesHelper,
) {
    var auth
        get() = preferences.get().getString(SharedPreferencesHelper.AUTH_KEY, "")
        set(value) = preferences.edit().putString(SharedPreferencesHelper.AUTH_KEY, value).apply()

    var selectedPupil
        get() = preferences.get().getInt(SharedPreferencesHelper.SELECTED_PUPIL_KEY, 0)
        set(value) = preferences.edit().putInt(SharedPreferencesHelper.SELECTED_PUPIL_KEY, value).apply()
}