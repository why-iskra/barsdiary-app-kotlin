package ru.unit.barsdiary.data.module.mark

import com.google.gson.Gson
import ru.unit.barsdiary.data.di.annotation.WebDateFormatter
import ru.unit.barsdiary.data.transformer.AttendanceChartTransformer
import ru.unit.barsdiary.data.transformer.MarksTransformer
import ru.unit.barsdiary.data.transformer.ProgressChartTransformer
import ru.unit.barsdiary.domain.mark.MarkService
import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import ru.unit.barsdiary.sdk.response.GetAttendanceChartResponseDTO
import ru.unit.barsdiary.sdk.response.GetProgressChartResponseDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MarkServiceImpl @Inject constructor(
    private val engine: BarsDiaryEngine,
    @WebDateFormatter private val webDateFormatter: DateTimeFormatter,

    private val marksTransformer: MarksTransformer,
    private val attendanceChartTransformer: AttendanceChartTransformer,
    private val progressChartTransformer: ProgressChartTransformer,
) : MarkService {
    override suspend fun getMarks(date: LocalDate): MarksPojo =
        marksTransformer.transform(engine.api { getSummaryMarks(webDateFormatter.format(date)) })

    override suspend fun getAttendanceChart(): AttendanceChartPojo? = attendanceChartTransformer.transform(
        engine.api {
            val data = getVisualizationData()

            val chart = data.chartsUrls?.attendance
            val periodBegin = data.periodBegin
            val periodEnd = data.periodEnd
            val studentIdParamName = data.studentIdParamName

            return@api if (chart != null && periodBegin != null && periodEnd != null && studentIdParamName != null) {
                Gson().fromJson(getChart(
                    if (chart.first() == '/') chart.drop(1) else chart,
                    periodBegin,
                    periodEnd,
                    0,
                    mapOf(studentIdParamName to data.pupilId.toString())
                ).string(), GetAttendanceChartResponseDTO::class.java)
            } else {
                null
            }
        }
    )

    override suspend fun getProgressChart(): ProgressChartPojo? = progressChartTransformer.transform(
        engine.api {
            val data = getVisualizationData()

            val chart = data.chartsUrls?.progress
            val periodBegin = data.periodBegin
            val periodEnd = data.periodEnd
            val studentIdParamName = data.studentIdParamName

            return@api if (chart != null && periodBegin != null && periodEnd != null && studentIdParamName != null) {
                Gson().fromJson(getChart(
                    if (chart.first() == '/') chart.drop(1) else chart,
                    periodBegin,
                    periodEnd,
                    0,
                    mapOf(studentIdParamName to data.pupilId.toString())
                ).string(), GetProgressChartResponseDTO::class.java)
            } else {
                null
            }
        }
    )

}