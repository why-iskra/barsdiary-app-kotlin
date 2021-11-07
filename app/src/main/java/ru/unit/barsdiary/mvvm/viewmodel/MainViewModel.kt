package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.unit.barsdiary.ApplicationStatus
import ru.unit.barsdiary.domain.auth.AuthUseCase
import ru.unit.barsdiary.sdk.exception.UnauthorizedException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val applicationStatus: ApplicationStatus,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    val throwableFlow = MutableLiveData<Throwable>()

    fun handleException(e: Throwable) {
        throwableFlow.postValue(e)
    }

    fun handleUnauthorizedException() {
        viewModelScope.launch(Dispatchers.Main) {
            authUseCase.logout()
        }
    }

    fun isOnline() = applicationStatus.isOnline()
}