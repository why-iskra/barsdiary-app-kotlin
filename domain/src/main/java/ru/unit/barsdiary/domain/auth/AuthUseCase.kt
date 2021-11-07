package ru.unit.barsdiary.domain.auth

import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import javax.inject.Inject

interface AuthUseCase {
    suspend fun auth(serverUrl: String?, login: String?, password: String?)
    suspend fun logout()
    suspend fun getServerList(): List<ServerInfoPojo>

    fun prepareFastAuth(): Boolean

    fun isParent(): Boolean
    fun getPupils(): List<ChildPojo>
    fun getSelectedPupil(): Int
    suspend fun changePupil(index: Int)

    fun isAuthorized(): Boolean
}

class AuthUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val authService: AuthService,
) : AuthUseCase {
    override suspend fun auth(serverUrl: String?, login: String?, password: String?) {
        val repositoryAuthData = authRepository.getAuthData()

        val authData = if (serverUrl.isNullOrEmpty() || login.isNullOrEmpty() || password.isNullOrEmpty()) {
            repositoryAuthData
        } else {
            AuthDataPojo(serverUrl.trimEnd('/'), login, password)
        }

        if (authData != null) {
            authService.auth(authData)
            authRepository.setAuthData(authData)
            authRepository.cleanRamCache()
        } else {
            throw Exception("Fields are empty")
        }
    }

    override suspend fun logout() {
        authRepository.clearAuthData()
//        authService.logout()
    }

    override suspend fun getServerList(): List<ServerInfoPojo> = authService.getServerList()

    override fun prepareFastAuth(): Boolean {
        val authData = authRepository.getAuthData() ?: return false

        authService.prepare(authData)
        return true
    }

    override fun isParent(): Boolean = authService.isParent()

    override fun getPupils(): List<ChildPojo> = authService.getChildren()

    override fun getSelectedPupil(): Int = authService.getSelectedChild()

    override suspend fun changePupil(index: Int) {
        authService.selectChild(index)
    }

    override fun isAuthorized(): Boolean {
        var success = false
        runCatching {
            success = authRepository.getAuthData() != null
        }
        return success
    }
}