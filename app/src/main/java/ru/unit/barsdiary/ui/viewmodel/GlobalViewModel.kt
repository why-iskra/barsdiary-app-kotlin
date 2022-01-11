package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.unit.barsdiary.data.di.annotation.AdvertBoardDateFormatter
import ru.unit.barsdiary.data.di.annotation.WebDateFormatter
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.*
import ru.unit.barsdiary.lib.HtmlUtils
import ru.unit.barsdiary.lib.livedata.EmptyLiveData
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.lib.livedata.ExceptionLiveData
import ru.unit.barsdiary.sdk.Engine
import ru.unit.barsdiary.ui.InAppNotifications
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(
    private val engine: Engine,
    private val globalUseCase: GlobalUseCase,
    private val inAppNotifications: InAppNotifications,
    @WebDateFormatter private val webDateTimeFormatter: DateTimeFormatter,
    @AdvertBoardDateFormatter private val advertBoardDateTimeFormatter: DateTimeFormatter
) : ViewModel() {

    companion object {
        private val birthdayDateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("d MMM, yyyy")
        private val advertBoardDateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("d MMM, yyyy")
        private val meetingDateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("d MMM, yyyy")
        private val classHourDateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("d MMM, yyyy")
        private val eventDateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("d MMM, yyyy")
    }

    val exceptionLiveData = ExceptionLiveData()

    val birthdaysLiveData = MutableLiveData<BirthdaysPojo>()
    val birthsTodayLiveData = MutableLiveData<Boolean>()
    val inBoxLiveData = MutableLiveData<Int>()
    val advertBoardLiveData = MutableLiveData<AdvertBoardPojo>()
    val meetingLiveData = MutableLiveData<MeetingPojo>()
    val classHourLiveData = MutableLiveData<ClassHourPojo>()
    val eventsLiveData = MutableLiveData<EventsPojo>()

    val resetBoxAdapterLiveData = EmptyLiveData()

    val eventLiveData = EventLiveData()

    private suspend fun getBirthdays() {
        exceptionLiveData.safety {
            val result = globalUseCase.getBirthdays()
            birthdaysLiveData.postValue(result)

            val has = result.birthdays.find {
                webIsToday(it.date)
            } != null

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

    private suspend fun getAdvertBoard() {
        exceptionLiveData.safety {
            advertBoardLiveData.postValue(globalUseCase.getAdvertBoard())
        }
    }

    private suspend fun getMeeting() {
        exceptionLiveData.safety {
            val meeting = globalUseCase.getMeeting()
            meetingLiveData.postValue(meeting)
            inAppNotifications.hasMeetingTodayLiveData.postValue(!meeting.date.isNullOrBlank())
        }
    }

    private suspend fun getClassHour() {
        exceptionLiveData.safety {
            val classHour = globalUseCase.getClassHour()
            classHourLiveData.postValue(classHour)
            inAppNotifications.hasClassHourTodayLiveData.postValue(!classHour.date.isNullOrBlank())
        }
    }

    private suspend fun getEvents() {
        exceptionLiveData.safety {
            val events = globalUseCase.getEvents()
            eventsLiveData.postValue(events)

            val countEvents = events.items.filter {
                !it.theme.isNullOrBlank() || !it.date.isNullOrBlank() || !it.dateStr.isNullOrBlank()
            }.count()

            inAppNotifications.hasEventsTodayLiveData.postValue(countEvents > 0)
        }
    }

    fun refreshAdvertBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            getAdvertBoard()

            eventLiveData.postEventLoaded()
        }
    }

    fun refreshMeeting() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            getMeeting()

            eventLiveData.postEventLoaded()
        }
    }

    fun refreshClassHour() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            getClassHour()

            eventLiveData.postEventLoaded()
        }
    }

    fun refreshEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            getEvents()

            eventLiveData.postEventLoaded()
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

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            resetBoxes()
            val asyncInBoxCount = async { getInBoxCount() }
            val asyncBirthdays = async { getBirthdays() }
            val asyncAdvertBoard = async { getAdvertBoard() }
            val asyncMeeting = async { getMeeting() }
            val asyncClassHour = async { getClassHour() }
            val asyncEvents = async { getEvents() }

            asyncInBoxCount.await()
            asyncBirthdays.await()
            asyncAdvertBoard.await()
            asyncMeeting.await()
            asyncClassHour.await()
            asyncEvents.await()

            eventLiveData.postEventLoaded()
        }
    }

    fun refreshNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            val asyncInBoxCount = async { getInBoxCount() }
            val asyncBirthdays = async { getBirthdays() }
            val asyncMeeting = async { getMeeting() }
            val asyncClassHour = async { getClassHour() }
            val asyncEvents = async { getEvents() }

            asyncInBoxCount.await()
            asyncBirthdays.await()
            asyncMeeting.await()
            asyncClassHour.await()
            asyncEvents.await()

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

    fun resetBirthdays() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clearBirthdays()
            }
        }
    }

    fun resetAdvertBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clearAdvertBoard()
            }
        }
    }

    fun resetMeeting() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clearMeeting()
            }
        }
    }

    fun resetClassHour() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clearClassHour()
            }
        }
    }

    fun resetEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                globalUseCase.clearEvents()
            }
        }
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

    fun birthdayDateFormat(date: String): String {
        exceptionLiveData.safety {
            val parsed = LocalDate.parse(date, webDateTimeFormatter)
            return birthdayDateFormatter.format(parsed)
        }

        return ""
    }

    fun advertBoardDateFormat(date: String?): String? {
        date ?: return null

        exceptionLiveData.safety {
            val parsed = LocalDate.parse(date, advertBoardDateTimeFormatter)
            return advertBoardDateFormatter.format(parsed)
        }

        return null
    }

    fun meetingDateFormat(date: String?): String? {
        date ?: return null

        exceptionLiveData.safety {
            val parsed = LocalDate.parse(date, webDateTimeFormatter)
            return meetingDateFormatter.format(parsed)
        }

        return null
    }

    fun classHourDateFormat(date: String?): String? {
        date ?: return null

        exceptionLiveData.safety {
            val parsed = LocalDate.parse(date, webDateTimeFormatter)
            return classHourDateFormatter.format(parsed)
        }

        return null
    }

    fun eventDateFormat(date: String?): String? {
        date ?: return null

        exceptionLiveData.safety {
            val parsed = LocalDate.parse(date, webDateTimeFormatter)
            return eventDateFormatter.format(parsed)
        }

        return null
    }

    fun webIsToday(value: String): Boolean =
        (value == webDateTimeFormatter.format(LocalDate.now()))

    fun document(name: String, url: String) =
        HtmlUtils.hrefDocument(HtmlUtils.prepareText(name), engine.getServerUrl() + url)
}