package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import ru.unit.barsdiary.data.di.annotation.WebDateFormatter
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.lib.HtmlUtils
import ru.unit.barsdiary.lib.livedata.EmptyLiveData
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.lib.livedata.ExceptionLiveData
import ru.unit.barsdiary.sdk.Engine
import ru.unit.barsdiary.ui.InAppNotifications
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(
    private val engine: Engine,
    private val globalUseCase: GlobalUseCase,
    @WebDateFormatter private val webDateTimeFormatter: DateTimeFormatter,
    private val inAppNotifications: InAppNotifications
) : ViewModel() {

    companion object {
        private val birthdayDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
    }

    val exceptionLiveData = ExceptionLiveData()

    val birthdaysLiveData = MutableLiveData<BirthdaysPojo>()
    val birthsTodayLiveData = MutableLiveData<Boolean>()
    val inBoxLiveData = MutableLiveData<Int>()

    val resetBoxAdapterLiveData = EmptyLiveData()

    val eventLiveData = EventLiveData()

    private suspend fun getBirthdays() {
        exceptionLiveData.safety {
            val result = globalUseCase.getBirthdays()
            birthdaysLiveData.postValue(result)

            val has = hasBirthsToday(result)
            birthsTodayLiveData.postValue(has)
            inAppNotifications.hasBirthsTodayLiveData.postValue(has)
        }
    }

    private suspend fun getInBoxCount() {
        exceptionLiveData.safety {
            val result = globalUseCase.getInBoxCount()
            inBoxLiveData.postValue(result)
            inAppNotifications.hasInBoxLiveData.postValue(result > 0)
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

            resetBoxes()
            val asyncInBoxCount = async { getInBoxCount() }
            val asyncBirthdays = async { getBirthdays() }

            asyncInBoxCount.await()
            asyncBirthdays.await()

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

            eventLiveData.postEventLoaded()
        }
    }

    fun silentRefreshInBoxCount() {
        viewModelScope.launch(Dispatchers.IO) {
            getInBoxCount()
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

    fun markRead(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.markRead(id)
                globalUseCase.clearInBoxCount()
                getInBoxCount()
            }
        }
    }

    private fun hasBirthsToday(value: BirthdaysPojo): Boolean {
        val date = LocalDate.now()
        return value.birthdays.find {
            return@find it.date == webDateTimeFormatter.format(date)
        } != null
    }

    fun document(name: String, url: String) = HtmlUtils.hrefDocument(HtmlUtils.prepareText(name), engine.getServerUrl() + url)
}