package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.auth.pojo.PupilPojo
import ru.unit.barsdiary.sdk.BarsEngine
import javax.inject.Inject

class PupilTransformer @Inject constructor() : BaseTransformer<BarsEngine.Pupil, PupilPojo> {
    override fun transform(value: BarsEngine.Pupil): PupilPojo {
        return PupilPojo(
            value.id,
            value.name,
            value.school
        )
    }

}