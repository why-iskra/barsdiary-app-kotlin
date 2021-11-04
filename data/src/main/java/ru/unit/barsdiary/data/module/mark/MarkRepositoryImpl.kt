package ru.unit.barsdiary.data.module.mark

import ru.unit.barsdiary.data.di.annotation.AttendanceChartRamCache
import ru.unit.barsdiary.data.di.annotation.MarksRamCache
import ru.unit.barsdiary.data.di.annotation.ProgressChartRamCache
import ru.unit.barsdiary.data.utils.RamCache
import ru.unit.barsdiary.domain.mark.MarkRepository
import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import java.time.LocalDate
import javax.inject.Inject

class MarkRepositoryImpl @Inject constructor(
    @MarksRamCache private val marksCache: RamCache<MarksPojo>,
    @AttendanceChartRamCache private val attendanceChartCache: RamCache<AttendanceChartPojo>,
    @ProgressChartRamCache private val progressChartCache: RamCache<ProgressChartPojo>,
) : MarkRepository {

    override suspend fun getMarks(date: LocalDate): MarksPojo? = marksCache.get(date)

    override suspend fun setMarks(value: MarksPojo, date: LocalDate) {
        marksCache.put(value, date)
    }

    override suspend fun clearMarks() {
        marksCache.clear()
    }

    override suspend fun getAttendanceChart(): AttendanceChartPojo? = attendanceChartCache.get()

    override suspend fun setAttendanceChart(value: AttendanceChartPojo) {
        attendanceChartCache.put(value)
    }

    override suspend fun clearAttendanceChart() {
        attendanceChartCache.clear()
    }

    override suspend fun getProgressChart(): ProgressChartPojo? = progressChartCache.get()

    override suspend fun setProgressChart(value: ProgressChartPojo) {
        progressChartCache.put(value)
    }

    override suspend fun clearProgressChart() {
        progressChartCache.clear()
    }

}