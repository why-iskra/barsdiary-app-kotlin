package ru.unit.barsdiary.data.module.auth

import ru.unit.barsdiary.data.sharedpreferences.AuthSharedPreferences
import ru.unit.barsdiary.data.utils.Safety
import ru.unit.barsdiary.domain.auth.AuthRepository
import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authPreferences: AuthSharedPreferences,
    private val safety: Safety,
) : AuthRepository {
    override fun setAuthData(authData: AuthDataPojo) {
        val encrypted = safety.encrypt(authData.serverUrl) + "$" + safety.encrypt(authData.login) + "$" + safety.encrypt(authData.password)
        authPreferences.auth = encrypted
    }

    override fun getAuthData(): AuthDataPojo? {
        val auth = authPreferences.auth

        return if (auth.isNullOrEmpty()) {
            null
        } else {
            val authFields = auth.split("$")
            val serverUrl = safety.decrypt(authFields[0])
            val login = safety.decrypt(authFields[1])
            val password = safety.decrypt(authFields[2])
            AuthDataPojo(serverUrl, login, password)
        }
    }

    override fun setSelectedPupil(value: Int) {
        authPreferences.selectedPupil = value
    }

    override fun getSelectedPupil(): Int = authPreferences.selectedPupil

    override fun clearAuthData() {
        authPreferences.auth = null
    }
}