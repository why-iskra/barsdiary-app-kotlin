package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.global.pojo.*
import ru.unit.barsdiary.sdk.response.*
import javax.inject.Inject

class EventsTransformer @Inject constructor() : BaseTransformer<GetEventsDTO, EventsPojo> {
    override fun transform(value: GetEventsDTO): EventsPojo {
        return EventsPojo(
            value.items.map { transform(it) }
        )
    }

    private fun transform(value: GetEventsItemDTO): EventPojo {
        return EventPojo(
            value.date,
            value.dateStr,
            value.theme
        )
    }

}