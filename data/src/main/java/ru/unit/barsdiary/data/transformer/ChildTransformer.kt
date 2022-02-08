package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.sdk.Engine
import javax.inject.Inject

class ChildTransformer @Inject constructor() : BaseTransformer<Engine.Child, ChildPojo> {
    override fun transform(value: Engine.Child): ChildPojo {
        return ChildPojo(
            value.id,
            value.name,
            value.school
        )
    }

}