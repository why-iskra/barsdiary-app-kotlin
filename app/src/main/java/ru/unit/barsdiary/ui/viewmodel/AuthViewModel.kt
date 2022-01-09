package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.domain.auth.AuthUseCase
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.lib.livedata.ExceptionLiveData
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    val serverUrlLiveData = MutableLiveData<String>()
    val loginLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()

    val authExceptionLiveData = ExceptionLiveData()
    val refreshExceptionLiveData = ExceptionLiveData()
    val serverListLiveData = MutableLiveData<List<ServerInfoPojo>>()
    val progressLiveData = MutableLiveData<Int>()

    val passwordVisibilityLiveData = MutableLiveData(false)

    val eventLiveData = EventLiveData()

    fun fastAuth(): Boolean {
        return settingsDataStore.fastAuth && authUseCase.prepareFastAuth()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            refreshExceptionLiveData.safety {
                serverListLiveData.postValue(authUseCase.getServerList())
            }

            eventLiveData.postEventLoaded()
        }
    }

    fun auth() {
        viewModelScope.launch(Dispatchers.IO) {
            authExceptionLiveData.safety {
                delay(2000)
                authUseCase.auth(serverUrlLiveData.value, loginLiveData.value, passwordLiveData.value)

                progressLiveData.postValue(50)
                delay(1500)
                progressLiveData.postValue(100)
            }
        }
    }

    fun isAuthorized() = authUseCase.isAuthorized()

    fun passwordVisibility() {
        val state = passwordVisibilityLiveData.value
        if (state == null) {
            passwordVisibilityLiveData.postValue(true)
        } else {
            passwordVisibilityLiveData.postValue(!state)
        }
    }

}