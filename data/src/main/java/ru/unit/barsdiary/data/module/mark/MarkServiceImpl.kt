package ru.unit.barsdiary.data.module.mark

import ru.unit.barsdiary.data.transformer.AttendanceChartTransformer
import ru.unit.barsdiary.data.transformer.MarksTransformer
import ru.unit.barsdiary.data.transformer.ProgressChartTransformer
import ru.unit.barsdiary.domain.mark.MarkService
import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import ru.unit.barsdiary.sdk.BarsWrapper
import java.time.LocalDate
import javax.inject.Inject

class MarkServiceImpl @Inject constructor(
    private val barsWrapper: BarsWrapper,
    private val marksTransformer: MarksTransformer,
    private val attendanceChartTransformer: AttendanceChartTransformer,
    private val progressChartTransformer: ProgressChartTransformer,
) : MarkService {
    override suspend fun getMarks(date: LocalDate): MarksPojo = marksTransformer.transform(barsWrapper.getSummaryMarks(date))

    override suspend fun getAttendanceChart(): AttendanceChartPojo? = attendanceChartTransformer.transform(barsWrapper.getAttendanceChart())

    override suspend fun getProgressChart(): ProgressChartPojo? = progressChartTransformer.transform(barsWrapper.getProgressChart())

}