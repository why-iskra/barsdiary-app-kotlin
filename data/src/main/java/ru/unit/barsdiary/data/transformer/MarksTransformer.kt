package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.mark.pojo.DisciplineMarksPojo
import ru.unit.barsdiary.domain.mark.pojo.MarkPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksSubPeriodPojo
import ru.unit.barsdiary.sdk.response.GetSummaryMarksDisciplineMarksDTO
import ru.unit.barsdiary.sdk.response.GetSummaryMarksMarkDTO
import ru.unit.barsdiary.sdk.response.GetSummaryMarksResponseDTO
import ru.unit.barsdiary.sdk.response.GetSummaryMarksSubPeriodDTO
import javax.inject.Inject

class MarksTransformer @Inject constructor() : BaseTransformer<GetSummaryMarksResponseDTO, MarksPojo> {
    override fun transform(value: GetSummaryMarksResponseDTO): MarksPojo {
        return MarksPojo(
            value.disciplineMarks.map { transform(it) },
            value.dates,
            transform(value.subPeriod)
        )
    }

    private fun transform(value: GetSummaryMarksDisciplineMarksDTO): DisciplineMarksPojo {
        return DisciplineMarksPojo(
            value.averageMark,
            value.marks.mapNotNull { transform(it) },
            value.discipline
        )
    }

    private fun transform(value: GetSummaryMarksMarkDTO): MarkPojo? {
        val mark = value.mark
        return if (mark.isNullOrBlank()) {
            null
        } else {
            MarkPojo(
                mark,
                value.description,
                value.date
            )
        }
    }

    private fun transform(value: GetSummaryMarksSubPeriodDTO?): MarksSubPeriodPojo? {
        val name = value?.name
        return if (value == null || name.isNullOrBlank()) {
            null
        } else {
            MarksSubPeriodPojo(
                name,
                value.code
            )
        }
    }
}