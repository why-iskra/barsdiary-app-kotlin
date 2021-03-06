package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.unit.barsdiary.domain.global.GlobalUseCase
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(
    private val globalUseCase: GlobalUseCase,
) : ViewModel() {

    fun markRead(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            globalUseCase.markRead(id)
            globalUseCase.clearInBoxCount()
            globalUseCase.getInBoxCount()
        }
    }

}