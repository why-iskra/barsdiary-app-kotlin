package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.unit.barsdiary.domain.auth.AuthUseCase
import ru.unit.barsdiary.sdk.exception.UnauthorizedException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    val unauthorizedFlow = MutableStateFlow(false)

    fun handleException(e: Throwable) {
        viewModelScope.launch(Dispatchers.Main) {
            if (e is UnauthorizedException) {
                handleUnauthorizedException()
            }
        }
    }

    private suspend fun handleUnauthorizedException() {
        authUseCase.logout()
        unauthorizedFlow.emit(true)
    }
}