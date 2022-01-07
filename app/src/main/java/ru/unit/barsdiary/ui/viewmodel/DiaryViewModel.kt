package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.unit.barsdiary.data.di.annotation.WebDateFormatter
import ru.unit.barsdiary.domain.diary.DiaryUseCase
import ru.unit.barsdiary.domain.diary.pojo.DiaryDayPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkDayPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkIndividualPojo
import ru.unit.barsdiary.domain.diary.pojo.MaterialPojo
import ru.unit.barsdiary.other.HtmlUtils
import ru.unit.barsdiary.other.livedata.EventLiveData
import ru.unit.barsdiary.other.livedata.ExceptionLiveData
import ru.unit.barsdiary.sdk.Engine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val engine: Engine,
    private val diaryUseCase: DiaryUseCase,
    @WebDateFormatter private val webDateTimeFormatter: DateTimeFormatter,
) : ViewModel() {
    companion object {
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")

        private fun getFirstDayOfWeek(date: LocalDate): LocalDate {
            return date.with(DayOfWeek.MONDAY)
        }
    }

    val exceptionLiveData = ExceptionLiveData()
    val updateFailIdLiveData = MutableLiveData<MutableList<Long>>()

    val dateLiveData = MutableLiveData(LocalDate.now())
    val dateStringLiveData = MutableLiveData(LocalDate.now().format(dateFormat))

    val lessonsLiveData = MutableLiveData(mutableMapOf<LocalDate, Pair<DiaryDayPojo, HomeworkDayPojo>>())

    val eventLiveData = EventLiveData()

    fun getLessons(id: Long, date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            exceptionLiveData.safety({
                val result = diaryUseCase.getDiary(getFirstDayOfWeek(date))
                val diary = result.first
                val homework = result.second

                val map = lessonsLiveData.value

                for ((i, day) in diary.days.withIndex()) {
                    map?.put(
                        LocalDate.parse(day.date, webDateTimeFormatter),
                        Pair(day, homework.days[i])
                    )
                }
                lessonsLiveData.postValue(map)

                val ids = updateFailIdLiveData.value ?: mutableListOf()
                if (ids.contains(id)) {
                    ids.remove(id)
                }
                updateFailIdLiveData.postValue(ids)
            }, {
                val ids = updateFailIdLiveData.value ?: mutableListOf()
                if (!ids.contains(id)) {
                    ids.add(id)
                }
                updateFailIdLiveData.postValue(ids)
            })

            eventLiveData.postEventLoaded()
        }
    }

    fun reset() {
        lessonsLiveData.value = mutableMapOf()
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                diaryUseCase.clear()
            }
        }
    }

    private fun document(name: String, url: String) = HtmlUtils.hrefDocument(HtmlUtils.prepareText(name), engine.getServerUrl() + url)

    fun materialsToString(list: List<MaterialPojo>) = buildString {
        val size = list.size
        for ((i, material) in list.withIndex()) {
            append(document(material.name, material.url))
            if (i != size - 1) {
                append(HtmlUtils.tagNewLine)
            }
        }
    }

    fun homeworkToString(homework: String, list: List<HomeworkIndividualPojo>) = buildString {
        append(homework)

        if (list.isNotEmpty()) {
            append(HtmlUtils.tagNewLine)
            append(HtmlUtils.tagNewLine)
            val size = list.size
            for ((i, individual) in list.withIndex()) {
                append(HtmlUtils.prepareText(individual.description ?: ""))
                append(" (")
                if (individual.document != null) {
                    val document = individual.document
                    append(
                        document(
                            document?.name ?: "",
                            document?.url ?: ""
                        )
                    )
                }
                append(")")
                if (i != size - 1) {
                    append(HtmlUtils.tagNewLine)
                }
            }
        }
    }

}