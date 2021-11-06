package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import javax.inject.Inject

class ChildTransformer @Inject constructor() : BaseTransformer<BarsDiaryEngine.Child, ChildPojo> {
    override fun transform(value: BarsDiaryEngine.Child): ChildPojo {
        return ChildPojo(
            value.id,
            value.name,
            value.school
        )
    }

}