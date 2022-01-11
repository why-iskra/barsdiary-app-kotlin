package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.unit.barsdiary.ui.components.ChartView
import ru.unit.barsdiary.data.di.annotation.ChartDateFormatter
import ru.unit.barsdiary.data.di.annotation.WebDateFormatter
import ru.unit.barsdiary.domain.mark.MarkUseCase
import ru.unit.barsdiary.domain.mark.pojo.DisciplineMarksPojo
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.lib.livedata.ExceptionLiveData

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val markUseCase: MarkUseCase,
    @WebDateFormatter private val webDateTimeFormatter: DateTimeFormatter,
    @ChartDateFormatter private val mobileDateTimeFormatter: DateTimeFormatter,
) : ViewModel() {

    companion object {

        private val chartDateFormatter = DateTimeFormatter.ofPattern("MMM")
    }

    val exceptionLiveData = ExceptionLiveData()

    val progressChartLiveData = MutableLiveData<Pair<Array<ChartView.LevelLine>, Array<ChartView.Element>>>()
    val attendanceLiveData = MutableLiveData<String>()
    val daysUntilVacationLiveData = MutableLiveData<String>()
    val averageGradeLiveData = MutableLiveData<String>()

    val marksLiveData = MutableLiveData<List<DisciplineMarksPojo>>()

    val eventLiveData = EventLiveData()

    private var attendanceText: String = ""
    private var daysUntilVacationText: String = ""
    private var averageGradeText: String = ""

    fun initialize(
        attendanceText: String,
        daysUntilVacationText: String,
        averageGradeText: String,
    ) {
        this.attendanceText = attendanceText
        this.daysUntilVacationText = daysUntilVacationText
        this.averageGradeText = averageGradeText
    }

    private suspend fun getProgressChart() {
        exceptionLiveData.safety {
            val response = markUseCase.getProgressChart() ?: return@safety

            val lines = mutableListOf<ChartView.LevelLine>()
            val elements = mutableListOf<ChartView.Element>()

            for ((i, value) in response.series[0].data.map { it.toFloat() }.withIndex()) {
                elements.add(
                    ChartView.Element(
                        LocalDate.parse(response.dates[i], mobileDateTimeFormatter)
                            .format(chartDateFormatter),
                        value
                    )
                )
            }

            for (i in 1..5) {
                lines.add(ChartView.LevelLine(i.toString(), i.toFloat()))
            }

            progressChartLiveData.postValue(Pair(lines.toTypedArray(), elements.toTypedArray()))
        }
    }

    private suspend fun getAttendance() {
        exceptionLiveData.safety {
            val response = markUseCase.getAttendanceChart() ?: return@safety
            attendanceLiveData.postValue(attendanceText + ": ${response.absent}")
        }
    }

    private suspend fun getDaysUntilVacation() {
        exceptionLiveData.safety {
            val dateNow = LocalDate.now()
            val response = markUseCase.getMarks(dateNow)

            val lastDate = LocalDate.parse(response.dates.last(), webDateTimeFormatter)

            val days = lastDate.toEpochDay() - dateNow.toEpochDay()

            daysUntilVacationLiveData.postValue(daysUntilVacationText + ": ${if (days < 0) 0 else days}")
        }
    }

    private suspend fun getAverageGrade() {
        exceptionLiveData.safety {
            val dateNow = LocalDate.now()
            val response = markUseCase.getMarks(dateNow)

            var sum = .0
            var count = 0

            response.disciplineMarks.forEach { discipline ->
                discipline.marks.forEach {
                    val mark = it.mark
                    if (mark.endsWith('-')) {
                        sum += it.mark.dropLast(1).toInt() - 0.5
                    } else {
                        sum += it.mark.toInt()
                    }
                    count++
                }
            }

            val result = sum / count

            val text = if(result.isNaN()) {
                "${averageGradeText}: 0"
            } else {
                averageGradeText + ": %.2f".format(
                    Locale.US,
                    result
                )
            }

            averageGradeLiveData.postValue(text)
        }
    }

    private suspend fun getMarksInner() {
        exceptionLiveData.safety {
            val dateNow = LocalDate.now()
            val response = markUseCase.getMarks(dateNow)

            marksLiveData.postValue(response.disciplineMarks)
        }
    }

    fun getMarks() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            getMarksInner()

            eventLiveData.postEventLoaded()
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            val asyncProgressChart = async { getProgressChart() }
            val asyncAttendanceChart = async { getAttendance() }
            val asyncDaysUntilVacation = async { getDaysUntilVacation() }
            val asyncAverageGrade = async { getAverageGrade() }
            val asyncMarks = async { getMarksInner() }

            asyncProgressChart.await()
            asyncAttendanceChart.await()
            asyncDaysUntilVacation.await()
            asyncAverageGrade.await()
            asyncMarks.await()

            eventLiveData.postEventLoaded()
        }
    }

    fun reset() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                markUseCase.clear()
            }
        }
    }

    fun resetMarksOnly() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                markUseCase.clearMarksOnly()
            }
        }
    }
}