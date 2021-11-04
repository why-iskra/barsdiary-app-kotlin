package ru.unit.barsdiary.domain.mark

import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import java.time.LocalDate

interface MarkRepository {
    suspend fun getMarks(date: LocalDate): MarksPojo?
    suspend fun setMarks(value: MarksPojo, date: LocalDate)
    suspend fun clearMarks()

    suspend fun getAttendanceChart(): AttendanceChartPojo?
    suspend fun setAttendanceChart(value: AttendanceChartPojo)
    suspend fun clearAttendanceChart()

    suspend fun getProgressChart(): ProgressChartPojo?
    suspend fun setProgressChart(value: ProgressChartPojo)
    suspend fun clearProgressChart()
}