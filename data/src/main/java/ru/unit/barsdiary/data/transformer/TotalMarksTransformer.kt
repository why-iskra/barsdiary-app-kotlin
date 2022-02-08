package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.person.pojo.PeriodMarkPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksDisciplinePojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksSubperiodPojo
import ru.unit.barsdiary.sdk.response.GetTotalMarksDisciplineMarksDTO
import ru.unit.barsdiary.sdk.response.GetTotalMarksPeriodMarkDTO
import ru.unit.barsdiary.sdk.response.GetTotalMarksResponseDTO
import ru.unit.barsdiary.sdk.response.GetTotalMarksSubperiodDTO
import javax.inject.Inject

class TotalMarksTransformer @Inject constructor() :
    BaseTransformer<GetTotalMarksResponseDTO, TotalMarksPojo> {
    override fun transform(value: GetTotalMarksResponseDTO): TotalMarksPojo {
        return TotalMarksPojo(
            value.disciplineMarks.map { transform(it) },
            value.subperiods.mapNotNull { transform(it) }
        )
    }

    private fun transform(value: GetTotalMarksSubperiodDTO): TotalMarksSubperiodPojo? {
        val code = value.code
        return if (code.isNullOrEmpty()) {
            null
        } else {
            TotalMarksSubperiodPojo(code, value.name)
        }
    }

    private fun transform(value: GetTotalMarksDisciplineMarksDTO): TotalMarksDisciplinePojo {
        return TotalMarksDisciplinePojo(
            value.discipline,
            value.periodMarks.mapNotNull { transform(it) }
        )
    }

    private fun transform(value: GetTotalMarksPeriodMarkDTO): PeriodMarkPojo? {
        val subperiodCode = value.subperiodCode
        return if (subperiodCode.isNullOrEmpty()) {
            null
        } else {
            PeriodMarkPojo(subperiodCode, value.mark)
        }
    }

}