package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.other.livedata.ExceptionLiveData
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(
    private val globalUseCase: GlobalUseCase,
) : ViewModel() {

    val exceptionLiveData = ExceptionLiveData()

    fun markRead(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.markRead(id)
                globalUseCase.clearInBoxCount()
                globalUseCase.getInBoxCount()
            }
        }
    }

}