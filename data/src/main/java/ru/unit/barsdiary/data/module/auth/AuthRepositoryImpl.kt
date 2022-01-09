package ru.unit.barsdiary.data.module.auth

import ru.unit.barsdiary.data.datastore.AuthDataStore
import ru.unit.barsdiary.data.utils.RamCacheCleaner
import ru.unit.barsdiary.domain.auth.AuthRepository
import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val ramCacheCleaner: RamCacheCleaner,
) : AuthRepository {
    override fun setAuthData(authData: AuthDataPojo) {
        authDataStore.serverUrl = authData.serverUrl
        authDataStore.login = authData.login
        authDataStore.password = authData.password
    }

    override fun getAuthData(): AuthDataPojo? {
        val serverUrl = authDataStore.serverUrl
        val login = authDataStore.login
        val password = authDataStore.password

        return if (serverUrl.isNullOrEmpty() || login.isNullOrEmpty() || password.isNullOrEmpty()) {
            null
        } else {
            AuthDataPojo(serverUrl, login, password)
        }
    }

    override fun clearAuthData() {
        authDataStore.serverUrl = null
        authDataStore.login = null
        authDataStore.password = null
        authDataStore.sessionId = null
    }

    override fun getSessionId(): String? = authDataStore.sessionId

    override fun setSessionId(id: String) {
        authDataStore.sessionId = id
    }

    override suspend fun cleanRamCache() {
        ramCacheCleaner.clean()
    }
}