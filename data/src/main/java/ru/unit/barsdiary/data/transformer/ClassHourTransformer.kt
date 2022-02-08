package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.global.pojo.ClassHourPojo
import ru.unit.barsdiary.sdk.response.GetClassHoursDTO
import javax.inject.Inject

class ClassHourTransformer @Inject constructor() :
    BaseTransformer<GetClassHoursDTO, ClassHourPojo> {
    override fun transform(value: GetClassHoursDTO): ClassHourPojo {
        return ClassHourPojo(
            value.date,
            value.begin,
            value.end,
            value.place,
            value.theme,
        )
    }
}