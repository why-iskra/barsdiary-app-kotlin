package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.global.pojo.BirthdayPojo
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.sdk.response.GetBirthdaysBirthdayDTO
import ru.unit.barsdiary.sdk.response.GetBirthdaysResponseDTO
import javax.inject.Inject

class BirthdaysTransformer @Inject constructor() : BaseTransformer<GetBirthdaysResponseDTO, BirthdaysPojo> {
    override fun transform(value: GetBirthdaysResponseDTO): BirthdaysPojo {
        return BirthdaysPojo(
            value.birthdays.mapNotNull { transform(it) }
        )
    }

    private fun transform(value: GetBirthdaysBirthdayDTO): BirthdayPojo? {
        val date = value.date
        return if (date.isNullOrEmpty()) {
            null
        } else {
            BirthdayPojo(
                date,
                value.photo,
                value.shortName,
                value.male
            )
        }
    }

}