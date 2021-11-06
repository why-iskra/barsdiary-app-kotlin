package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.unit.barsdiary.data.di.annotation.WebDateFormatter
import ru.unit.barsdiary.data.utils.aTagDocument
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.other.livedata.EmptyLiveData
import ru.unit.barsdiary.other.livedata.EventLiveData
import ru.unit.barsdiary.other.livedata.ExceptionLiveData
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(
    private val barsDiaryEngine: BarsDiaryEngine,
    private val globalUseCase: GlobalUseCase,
    @WebDateFormatter private val webDateTimeFormatter: DateTimeFormatter,
) : ViewModel() {

    companion object {
        private val birthdayDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
    }

    val exceptionLiveData = ExceptionLiveData()

    val birthdaysLiveData = MutableLiveData<BirthdaysPojo>()

    val birthsTodayLiveData = MutableLiveData<Boolean>()
    val hasInBoxLiveData = MutableLiveData<Boolean>()

    val notificationLiveData = EmptyLiveData()
    val resetBoxAdapterLiveData = EmptyLiveData()

    val eventLiveData = EventLiveData()

    private suspend fun getBirthdays() {
        exceptionLiveData.safety {
            val result = globalUseCase.getBirthdays()
            birthdaysLiveData.postValue(result)
            birthsTodayLiveData.postValue(hasBirthsToday(result))
        }
    }

    private suspend fun getInBoxCount() {
        exceptionLiveData.safety {
            val result = globalUseCase.getInBoxCount()
            hasInBoxLiveData.postValue(result > 0)
        }
    }

    fun refreshBirthdays() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            getBirthdays()

            eventLiveData.postEventLoaded()
        }
    }

    fun refreshBoxes() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            delay(1000)
            resetBoxAdapterLiveData.post()

            eventLiveData.postEventLoaded()
        }
    }

    fun resetBirthdays() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clearBirthdays()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            val asyncInBoxCount = async { getInBoxCount() }
            val asyncBirthdays = async { getBirthdays() }

            asyncInBoxCount.await()
            asyncBirthdays.await()

            notificationLiveData.post()
            eventLiveData.postEventLoaded()
        }
    }

    fun refreshNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            val asyncInBoxCount = async { getInBoxCount() }
            val asyncBirthdays = async { getBirthdays() }

            asyncInBoxCount.await()
            asyncBirthdays.await()

            notificationLiveData.post()
            eventLiveData.postEventLoaded()
        }
    }

    fun reset() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clear()
            }
        }
    }

    fun resetBoxes() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clearInBox()
                globalUseCase.clearOutBox()
            }
        }
    }

    fun birthdayDateFormat(date: String): String {
        exceptionLiveData.safety {
            val parsed = LocalDate.parse(date, webDateTimeFormatter)
            return birthdayDateFormatter.format(parsed)
        }

        return ""
    }

    fun deleteInBox(list: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.removeInBoxMessages(list)
                refreshBoxes()
            }
        }
    }

    fun deleteOutBox(list: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.removeOutBoxMessages(list)
                refreshBoxes()
            }
        }
    }

    private fun hasBirthsToday(value: BirthdaysPojo): Boolean {
        val date = LocalDate.now()
        return value.birthdays.find {
            return@find it.date == webDateTimeFormatter.format(date)
        } != null
    }

    fun document(name: String, url: String) = aTagDocument(name, barsDiaryEngine.getServerUrl() + url)
}