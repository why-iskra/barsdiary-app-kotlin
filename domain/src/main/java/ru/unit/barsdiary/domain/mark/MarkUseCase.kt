package ru.unit.barsdiary.domain.mark

import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import java.time.LocalDate
import javax.inject.Inject

interface MarkUseCase {
    suspend fun getMarks(date: LocalDate): MarksPojo
    suspend fun getAttendanceChart(): AttendanceChartPojo?
    suspend fun getProgressChart(): ProgressChartPojo?
    suspend fun clear()
    suspend fun clearMarksOnly()
}

class MarkUseCaseImpl @Inject constructor(
    private val markRepository: MarkRepository,
    private val markService: MarkService,
) : MarkUseCase {

    override suspend fun getMarks(date: LocalDate): MarksPojo =
        markRepository.getMarks(date) ?: markService.getMarks(date).apply { markRepository.setMarks(this, date) }

    override suspend fun getAttendanceChart(): AttendanceChartPojo? =
        markRepository.getAttendanceChart() ?: markService.getAttendanceChart().apply { if (this != null) markRepository.setAttendanceChart(this) }

    override suspend fun getProgressChart(): ProgressChartPojo? =
        markRepository.getProgressChart() ?: markService.getProgressChart().apply { if (this != null) markRepository.setProgressChart(this) }

    override suspend fun clear() {
        markRepository.clearMarks()
        markRepository.clearAttendanceChart()
        markRepository.clearProgressChart()
    }

    override suspend fun clearMarksOnly() {
        markRepository.clearMarks()
    }

}