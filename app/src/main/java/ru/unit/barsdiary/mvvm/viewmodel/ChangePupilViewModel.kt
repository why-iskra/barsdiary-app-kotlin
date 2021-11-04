package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.unit.barsdiary.domain.auth.AuthUseCase
import ru.unit.barsdiary.domain.diary.DiaryUseCase
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.mark.MarkUseCase
import ru.unit.barsdiary.domain.person.PersonUseCase
import ru.unit.barsdiary.other.SpamGuard
import ru.unit.barsdiary.other.livedata.EventLiveData
import ru.unit.barsdiary.other.livedata.ExceptionLiveData
import javax.inject.Inject

@HiltViewModel
class ChangePupilViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val diaryUseCase: DiaryUseCase,
    private val markUseCase: MarkUseCase,
    private val personUseCase: PersonUseCase,
    private val globalUseCase: GlobalUseCase,
) : ViewModel() {

    private val spamGuard = SpamGuard()

    val exceptionLiveData = ExceptionLiveData()
    val eventLiveData = EventLiveData()

    fun getPupils() = authUseCase.getPupils()
    fun getSelectedPupil() = authUseCase.getSelectedPupil()

    fun changeChild(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            spamGuard.guard("changeChild") {
                eventLiveData.postEventLoading()

                exceptionLiveData.safety {
                    authUseCase.changePupil(index)
                    clearAll()
                }

                delay(1000)

                eventLiveData.postEventLoaded()

                it.unlock()
            }
        }
    }

    private suspend fun clearAll() {
        diaryUseCase.clear()
        markUseCase.clear()
        personUseCase.clear()
        globalUseCase.clear()
    }

}