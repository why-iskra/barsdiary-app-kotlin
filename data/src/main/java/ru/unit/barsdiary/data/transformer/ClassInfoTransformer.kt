package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.ClassmatePojo
import ru.unit.barsdiary.sdk.response.GetClassYearInfoClassmateDTO
import ru.unit.barsdiary.sdk.response.GetClassYearInfoResponseDTO
import javax.inject.Inject

class ClassInfoTransformer @Inject constructor() :
    BaseTransformer<GetClassYearInfoResponseDTO, ClassInfoPojo> {
    override fun transform(value: GetClassYearInfoResponseDTO): ClassInfoPojo {
        return ClassInfoPojo(
            value.formMaster,
            value.formMasterMale,
            value.letter,
            value.studyLevel,
            value.classmates.map { transform(it) }
        )
    }

    private fun transform(value: GetClassYearInfoClassmateDTO): ClassmatePojo {
        return ClassmatePojo(
            value.name,
            value.male
        )
    }


}