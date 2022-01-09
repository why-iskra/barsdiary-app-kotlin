package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.unit.barsdiary.SystemServices
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.domain.auth.AuthRepository
import ru.unit.barsdiary.domain.auth.AuthUseCase
import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.domain.diary.DiaryUseCase
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.mark.MarkUseCase
import ru.unit.barsdiary.domain.person.PersonUseCase
import ru.unit.barsdiary.lib.InAppLog
import ru.unit.barsdiary.lib.livedata.ExceptionLiveData
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DeveloperViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val diaryUseCase: DiaryUseCase,
    private val markUseCase: MarkUseCase,
    private val personUseCase: PersonUseCase,
    private val globalUseCase: GlobalUseCase,
    private val authRepository: AuthRepository,
    private val inAppLog: InAppLog,
    private val systemServices: SystemServices,
    val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val updateLogFlow = inAppLog.publicUpdateFlow

    val exceptionLiveData = ExceptionLiveData()

    fun getLog() = inAppLog.publicLogList

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                diaryUseCase.clear()
                markUseCase.clear()
                personUseCase.clear()
                globalUseCase.clear()
            }
        }
    }

    fun fakeAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                val serverList = authUseCase.getServerList()

                val server = serverList[Random.nextInt(0, serverList.size)].url
                val login = randomString(8)
                val password = randomString(16)

                authUseCase.auth(
                    server,
                    login,
                    password
                )

                authRepository.setAuthData(
                    AuthDataPojo(
                        server,
                        login,
                        password
                    )
                )

                authRepository.setSessionId(randomString(32))
            }
        }
    }

    fun crash() {
        throw RuntimeException()
    }

    fun getSessionId(): String? = authRepository.getSessionId()

    fun sessionIdToClipboard() {
        systemServices.plainTextClipboard(
            "SessionId",
            getSessionId().toString()
        )
    }

    private fun randomString(len: Int): String {
        val charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        return (1..len).map { Random.nextInt(0, charPool.length) }
            .map(charPool::get)
            .joinToString("")
    }
}