package ru.unit.barsdiary.domain.mark

import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import java.time.LocalDate

interface MarkService {
    suspend fun getMarks(date: LocalDate): MarksPojo
    suspend fun getAttendanceChart(): AttendanceChartPojo?
    suspend fun getProgressChart(): ProgressChartPojo?
}