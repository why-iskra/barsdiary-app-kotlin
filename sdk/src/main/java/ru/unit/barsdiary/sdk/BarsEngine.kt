package ru.unit.barsdiary.sdk

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.unit.barsdiary.sdk.di.annotation.ApiWebClient
import ru.unit.barsdiary.sdk.exception.FinishRegistrationAccountException
import ru.unit.barsdiary.sdk.response.GetPersonDataResponseDTO
import ru.unit.barsdiary.sdk.response.WebLoginResponseDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarsEngine @Inject constructor(
    @ApiWebClient val webClient: HttpClient,
) {
    companion object {
        const val webDateFormatPattern = "yyyy-MM-dd"
        const val chartDateFormatPattern = "dd.MM.yyyy"
        const val mailDateFormatPattern = "dd.MM.yyyy HH:mm"

        fun getServerList(): List<ServerInfo> {
            return listOf(
                ServerInfo("https://xn--80atdl2c.xn--33-6kcadhwnl3cfdx.xn--p1ai", "Владимирская область"),
                ServerInfo("https://school.vip.edu35.ru", "Вологодская область"),
                ServerInfo("https://school.07.edu.o7.com/", "Кабардино-Балкарская республика"),
                ServerInfo("https://schools48.ru", "Липецкая область"),
                ServerInfo("https://s51.edu.o7.com", "Мурманская область"),
                ServerInfo("https://edu.adm-nao.ru/", "Ненецкий автономный округ"),
                ServerInfo("https://shkola.nso.ru", "Новосибирская область"),
                ServerInfo("https://sosh.mon-ra.ru", "Республика Алтай"),
                ServerInfo("https://school.karelia.ru/", "Республика Карелия"),
                ServerInfo("https://school.r-19.ru", "Республика Хакасия"),
                ServerInfo("https://sh-open.ris61edu.ru/", "Ростовская область "),
                ServerInfo("https://e-school.ryazangov.ru/", "Рязанская область"),
                ServerInfo("https://es.ciur.ru", "Удмуртская республика")
            )
        }
    }

    var selectedPupil: Int = 0

    lateinit var authData: AuthData

    private var isParent: Boolean = false
    private val _pupils: MutableList<Pupil> = mutableListOf()
    val pupils: List<Pupil> get() = _pupils

    suspend fun auth(authData: AuthData) = coroutineScope {
        this@BarsEngine.authData = authData

        val serverUrl = authData.serverUrl
        val login = authData.login
        val password = authData.password

        val responseWeb = async {
            val resultLogin = webClient.submitForm<WebLoginResponseDTO>(serverUrl + RequestUrls.Web.Login, Parameters.build {
                append("login_login", login)
                append("login_password", password)
            }, encodeInQuery = false)

//            webClient.get<HttpResponse>(serverUrl + resultLogin.redirect)
            val url = webClient.get<HttpResponse>(serverUrl + resultLogin.redirect).request.url.toString()
            if (url != "$serverUrl/personal-area/") {
                throw FinishRegistrationAccountException(serverUrl)
            }

            webClient.get<GetPersonDataResponseDTO>(serverUrl + RequestUrls.Web.ProfileService.GetPersonData)
        }

        val webData = responseWeb.await()

        _pupils.clear()
        if (webData.childrenPersons.isNotEmpty()) {
            isParent = true

            val size = webData.childrenPersons.size
            for (i in 0 until size) {
                _pupils.add(
                    Pupil(
                        webData.childrenPersons[i].personId,
                        webData.childrenPersons[i].fullName,
                        webData.childrenPersons[i].school
                    )
                )
            }

            changePupil(selectedPupil)
        } else {
            isParent = false
            selectedPupil = 0
        }
    }

    suspend fun changePupil(index: Int) {
        if (!isParent) return

        var i = index
        if (i < 0) {
            i = 0
        } else if (i >= pupils.size) {
            i = pupils.size - 1
        }

        val saved = selectedPupil
        selectedPupil = i
        runCatching {
            webClient.submitForm<HttpResponse>(
                authData.serverUrl + RequestUrls.Web.ProfileService.SetChild,
                Parameters.build { append("selected", _pupils[selectedPupil].id.toString()) },
                encodeInQuery = false
            )
        }.onFailure {
            selectedPupil = saved
            throw it
        }
    }

    suspend inline fun <reified T> submitFormWeb(apiMethodUrl: String, params: Parameters, encodeInQuery: Boolean): T {
        return webClient.submitForm(authData.serverUrl + apiMethodUrl, params, encodeInQuery)
    }

    suspend inline fun <reified T> authProtectSubmitFormWeb(
        apiMethodUrl: String,
        params: Parameters,
        encodeInQuery: Boolean,
        enable: Boolean = true,
    ): T {
        if (enable) {
            repeat(3) {
                try {
                    return submitFormWeb(apiMethodUrl, params, encodeInQuery)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            auth(authData)
        }
        return submitFormWeb(apiMethodUrl, params, encodeInQuery)
    }

    suspend fun logout() = coroutineScope {
        webClient.get<HttpResponse>(authData.serverUrl + RequestUrls.Web.Logout)
    }

    private fun document(url: String): String = authData.serverUrl + url
    fun document(name: String, url: String): String = "<a href=\"%s\">%s</a>".format(document(url), name)

    fun isParent() = isParent

    data class Pupil(
        val id: Int,
        val name: String?,
        val school: String?,
    )

    data class ServerInfo(
        val url: String,
        val name: String,
    )

    data class AuthData(
        var serverUrl: String,
        var login: String,
        var password: String,
    )
}