package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.global.pojo.MeetingPojo
import ru.unit.barsdiary.sdk.response.GetMeetingDTO
import javax.inject.Inject

class MeetingTransformer @Inject constructor() : BaseTransformer<GetMeetingDTO, MeetingPojo> {
    override fun transform(value: GetMeetingDTO): MeetingPojo {
        return MeetingPojo(
            value.date,
            value.begin,
            value.end,
            value.id,
            value.office,
            value.protocol,
        )
    }
}