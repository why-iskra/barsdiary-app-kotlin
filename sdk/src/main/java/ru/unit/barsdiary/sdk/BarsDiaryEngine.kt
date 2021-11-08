package ru.unit.barsdiary.sdk

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.unit.barsdiary.sdk.exception.AuthDataNotInitException
import ru.unit.barsdiary.sdk.exception.FinishRegistrationAccountException
import ru.unit.barsdiary.sdk.exception.UnauthorizedException

class BarsDiaryEngine(
    private val client: OkHttpClient,
) {

    private fun retrofitBuilder(url: String) = Retrofit.Builder()
        .client(client)
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var _service: ApiService? = null

    private val service: ApiService
        get() {
            val url = authData?.serverUrl ?: throw AuthDataNotInitException()

            if (_service == null) {
                _service = retrofitBuilder(url).create(ApiService::class.java)
            }

            return _service!!
        }

    private var authData: AuthData? = null
    private var inited: Boolean = false
    private var parent: Boolean = false
    private var childrenList: List<Child> = emptyList()
    private var selectedChild: Int = 0

    private val authMutex = Mutex()
    private val logicMutex = Mutex()

    fun setAuthData(data: AuthData) {
        this.authData = data
        _service = null

        runCatching { authMutex.unlock() }
        runCatching { logicMutex.unlock() }
    }

    private suspend fun waitAuth() {
        authMutex.withLock { }
    }

    suspend fun auth() {
        auth(false)
    }

    private suspend fun auth(skipAuth: Boolean) {
        logicMutex.lock()

        if (authMutex.isLocked) {
            logicMutex.unlock()

            waitAuth()
            return
        }

        authMutex.lock()
        logicMutex.unlock()

        runCatching {
            if (!skipAuth) {
                val data = authData ?: throw AuthDataNotInitException()

                try {
                    val result = service.login(data.login, data.password)

                    if (!result.success) throw UnauthorizedException()
                    val redirect = result.redirect.orEmpty()

                    val parsedRedirect = if (redirect.isNotEmpty() && redirect.first() == '/') redirect.drop(1) else redirect

                    val redirectUrl = service.redirect(parsedRedirect).raw().request().url().toString()

                    if ("${data.serverUrl}/personal-area/" != redirectUrl) {
                        runCatching {
                            service.noinput(redirectUrl.removePrefix("${data.serverUrl}/"))
                        }.onFailure {
                            throw FinishRegistrationAccountException(data.serverUrl)
                        }
                    }
                } catch (e: HttpException) {
                    if (e.code() == 401 || e.code() == 403) {
                        throw UnauthorizedException()
                    } else {
                        throw e
                    }
                }
            }

            updateParentData()
        }.onFailure {
            authMutex.unlock()
            throw it
        }

        authMutex.unlock()
    }

    private suspend fun updateParentData() {
        val personData = service.getPersonData()
        if (personData.childrenPersons.isNotEmpty()) {
            parent = true

            childrenList = personData.childrenPersons.map {
                Child(
                    it.personId,
                    it.fullName,
                    it.school
                )
            }.toList()

            changeChild(selectedChild)
        } else {
            parent = false
            selectedChild = 0
        }

        inited = true
    }

    suspend fun changeChild(index: Int) {
        if (!parent) return

        var i = index
        if (i < 0) {
            i = 0
        } else if (i >= childrenList.size) {
            i = childrenList.size - 1
        }

        val saved = selectedChild
        selectedChild = i
        runCatching {
            service.setChild(childrenList[selectedChild].id)
        }.onFailure {
            selectedChild = saved
            throw it
        }
    }

    suspend fun logout() {
        waitAuth()
        service.logout()
    }

    suspend fun <T> api(authProtect: Boolean = true, block: suspend ApiService.() -> T): T {
        waitAuth()

        runCatching {
            if (!inited) {
                auth(true)
            }
        }

        return if (authProtect) {
            return try {
                block.invoke(service)
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    auth(false)
                    block.invoke(service)
                } else {
                    throw e
                }
            }
        } else {
            block.invoke(service)
        }
    }

    fun getServerUrl() = authData?.serverUrl ?: throw AuthDataNotInitException()
    fun isParent() = parent
    fun children() = childrenList
    fun selectedChild() = selectedChild

    data class Child(
        val id: Int,
        val name: String?,
        val school: String?,
    )

    data class AuthData(
        val serverUrl: String,
        val login: String,
        val password: String,
    )

    companion object {
        const val webDateFormatPattern = "yyyy-MM-dd"
        const val chartDateFormatPattern = "dd.MM.yyyy"
        const val mailDateFormatPattern = "dd.MM.yyyy HH:mm"
    }
}