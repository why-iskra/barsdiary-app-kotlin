package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.diary.pojo.DiaryDayPojo
import ru.unit.barsdiary.domain.diary.pojo.DiaryLessonPojo
import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.MaterialPojo
import ru.unit.barsdiary.sdk.response.GetDiaryDayDTO
import ru.unit.barsdiary.sdk.response.GetDiaryLessonDTO
import ru.unit.barsdiary.sdk.response.GetDiaryMaterialDTO
import ru.unit.barsdiary.sdk.response.GetDiaryResponseDTO
import javax.inject.Inject

class DiaryTransformer @Inject constructor() : BaseTransformer<GetDiaryResponseDTO, DiaryPojo> {
    override fun transform(value: GetDiaryResponseDTO): DiaryPojo {
        return DiaryPojo(
            value.days.mapNotNull { transform(it) }
        )
    }

    private fun transform(value: GetDiaryDayDTO): DiaryDayPojo? {
        val date = value.date
        return if (date.isNullOrBlank()) {
            null
        } else {
            DiaryDayPojo(
                date,
                value.lessons?.map {
                    transform(it)
                },
                value.isVacation ?: false,
                value.isHoliday ?: false,
                value.isWeekend ?: false
            )
        }
    }

    private fun transform(value: GetDiaryLessonDTO): DiaryLessonPojo {
        return DiaryLessonPojo(
            value.homework,
            value.teacher,
            value.remarks,
            value.studyTimeName,
            value.materials.mapNotNull { transform(it) },
            value.theme,
            value.date,
            value.mark,
            value.timeEnd,
            value.timeBegin,
            value.scheduleLessonType,
            value.index,
            value.office,
            value.attendance,
            value.discipline,
            value.comment
        )
    }

    private fun transform(value: GetDiaryMaterialDTO): MaterialPojo? {
        val url = value.url
        return if (url.isNullOrBlank()) {
            null
        } else {
            MaterialPojo(
                value.name ?: "",
                url
            )
        }
    }

}