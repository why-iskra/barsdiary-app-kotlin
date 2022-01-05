package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.unit.barsdiary.SystemServices
import ru.unit.barsdiary.domain.auth.AuthUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val systemServices: SystemServices,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    val navigationBarShowLiveData = MutableLiveData<Boolean?>(null)

    val throwableLiveData = MutableLiveData<Throwable>()

    fun handleException(e: Throwable) {
        throwableLiveData.postValue(e)
    }

    fun handleUnauthorizedException() {
        viewModelScope.launch(Dispatchers.Main) {
            authUseCase.logout()
        }
    }

    fun isOnline() = systemServices.isOnline()

    fun hideNavigationBar() {
        navigationBarShowLiveData.postValue(false)
    }

    fun showNavigationBar() {
        navigationBarShowLiveData.postValue(true)
    }
}