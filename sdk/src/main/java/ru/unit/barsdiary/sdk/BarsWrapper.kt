package ru.unit.barsdiary.sdk

import io.ktor.http.*
import ru.unit.barsdiary.sdk.di.annotation.WebDateFormatter
import ru.unit.barsdiary.sdk.response.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarsWrapper @Inject constructor(
    private val barsEngine: BarsEngine,
    @WebDateFormatter private val webDateFormatter: DateTimeFormatter
) {
    suspend fun getPersonData(): GetPersonDataResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.ProfileService.GetPersonData, Parameters.build { }, true)
    }

    suspend fun getDiary(date: LocalDate): GetDiaryResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.ScheduleService.GetDiary, Parameters.build {
            append("date", date.format(webDateFormatter))
            append("is_diary", "true")
        }, true)
    }

    suspend fun getHomework(date: LocalDate): GetHomeworkFromRangeResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.HomeworkService.GetHomeworkFromRange, Parameters.build {
            append("date", date.format(webDateFormatter))
        }, true)
    }

    suspend fun getVisualizationData(): GetVisualizationDataResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.MarkService.GetVisualizationData, Parameters.build {}, true)
    }

    suspend fun getProgressChart(): GetProgressChartResponseDTO? {
        val data = getVisualizationData()

        return if (data.chartsUrls?.progress != null && data.periodBegin != null && data.periodEnd != null && data.studentIdParamName != null) {
            barsEngine.authProtectSubmitFormWeb(data.chartsUrls.progress, Parameters.build {
                append("date_begin", data.periodBegin)
                append("date_end", data.periodEnd)
                append("subject", "0")
                append(data.studentIdParamName, data.pupilId.toString())
            }, true)
        } else {
            null
        }
    }

    suspend fun getAttendanceChart(): GetAttendanceChartResponseDTO? {
        val data = getVisualizationData()

        return if (data.chartsUrls?.attendance != null && data.periodBegin != null && data.periodEnd != null && data.studentIdParamName != null) {
            barsEngine.authProtectSubmitFormWeb(data.chartsUrls.attendance, Parameters.build {
                append("date_begin", data.periodBegin)
                append("date_end", data.periodEnd)
                append("subject", "0")
                append(data.studentIdParamName, data.pupilId.toString())
            }, true)
        } else {
            null
        }
    }

    suspend fun getSummaryMarks(date: LocalDate): GetSummaryMarksResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.MarkService.GetSummaryMarks, Parameters.build {
            append("date", date.format(webDateFormatter))
        }, true)
    }

    suspend fun getSchoolInfo(): GetSchoolInfoResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.SchoolService.GetSchoolInfo, Parameters.build {}, true)
    }

    suspend fun getClassYearInfo(): GetClassYearInfoResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.SchoolService.GetClassYearInfo, Parameters.build {}, true)
    }

    suspend fun getTotalMarks(): GetTotalMarksResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.MarkService.GetTotalMarks, Parameters.build {}, true)
    }

    suspend fun getOutBoxMessages(page: Int): GetBoxMessagesResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.MailBoxService.GetOutBoxMessages, Parameters.build {
            append("page", page.toString())
        }, true)
    }

    suspend fun getInBoxMessages(page: Int): GetBoxMessagesResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.MailBoxService.GetInBoxMessages, Parameters.build {
            append("page", page.toString())
        }, true)
    }

    suspend fun getInBoxCount(): Int {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.MailBoxService.GetInBoxCount, Parameters.build {}, true)
    }

    suspend fun markRead(id: Int) {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.MailBoxService.MarkRead, Parameters.build {
            append("message_id", id.toString())
        }, false)
    }

    suspend fun getBirthdays(): GetBirthdaysResponseDTO {
        return barsEngine.authProtectSubmitFormWeb(RequestUrls.Web.WidgetService.GetBirthdays, Parameters.build {}, true)
    }
}