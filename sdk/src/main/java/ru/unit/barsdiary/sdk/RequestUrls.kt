package ru.unit.barsdiary.sdk

object RequestUrls {
    object Web {
        const val Login = "/auth/login"
        const val Logout = "/auth/force-logout"

        object ProfileService {
            const val GetPersonData = "/api/ProfileService/GetPersonData"
            const val SetChild = "/api/ProfileService/SetChild"
        }

        object ScheduleService {
            const val GetDiary = "/api/ScheduleService/GetDiary"
        }

        object HomeworkService {
            const val GetHomeworkFromRange = "/api/HomeworkService/GetHomeworkFromRange"
        }

        object MarkService {
            const val GetVisualizationData = "/api/MarkService/GetVisualizationData"
            const val GetSummaryMarks = "/api/MarkService/GetSummaryMarks"
            const val GetTotalMarks = "/api/MarkService/GetTotalMarks"
        }

        object SchoolService {
            const val GetSchoolInfo = "/api/SchoolService/getSchoolInfo"
            const val GetClassYearInfo = "/api/SchoolService/getClassYearInfo"
        }

        object WidgetService {
            const val GetBirthdays = "/api/WidgetService/getBirthdays"
        }

        object MailBoxService {
            const val GetOutBoxMessages = "/api/MailBoxService/getOutBoxMessages"
            const val GetInBoxMessages = "/api/MailBoxService/getInBoxMessages"
            const val GetInBoxCount = "/api/MailBoxService/getInBoxCount"
            const val MarkRead = "/api/MailBoxService/markRead"
        }
    }

    object Mobile {
        const val Login = "/rest/login"
        const val Logout = "/rest/login"
    }
}