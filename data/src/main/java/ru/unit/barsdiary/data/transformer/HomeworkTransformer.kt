package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.diary.pojo.*
import ru.unit.barsdiary.sdk.response.*
import javax.inject.Inject

class HomeworkTransformer @Inject constructor() :
    BaseTransformer<GetHomeworkFromRangeResponseDTO, HomeworkPojo> {
    override fun transform(value: GetHomeworkFromRangeResponseDTO): HomeworkPojo {
        return HomeworkPojo(
            value.days.mapNotNull { transform(it) }
        )
    }

    private fun transform(value: GetHomeworkFromRangeDayDTO): HomeworkDayPojo? {
        val date = value.date
        return if (date.isNullOrBlank()) {
            null
        } else {
            HomeworkDayPojo(
                date,
                value.homeworks.map { transform(it) }
            )
        }
    }

    private fun transform(value: GetHomeworkFromRangeHomeworkDTO): HomeworkLessonPojo {
        return HomeworkLessonPojo(
            value.homework,
            value.teacher,
            value.date,
            value.materials.mapNotNull { transform(it) },
            value.theme,
            value.scheduleLessonType,
            value.individualHomeworks.map { transform(it) },
            value.discipline,
            value.nextHomework,
            value.nextMaterials.mapNotNull { transform(it) },
            value.nextIndividualHomeworks.map { transform(it) }
        )
    }

    private fun transform(value: GetHomeworkFromRangeMaterialDTO?): MaterialPojo? {
        val url = value?.url
        return if (value == null || url.isNullOrBlank()) {
            null
        } else {
            MaterialPojo(
                value.name ?: "",
                url
            )
        }
    }

    private fun transform(value: GetHomeworkFromRangeIndividualHomeworkDTO): HomeworkIndividualPojo {
        return HomeworkIndividualPojo(
            value.description,
            transform(value.document)
        )
    }
}