package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.global.pojo.AdvertBoardItemPojo
import ru.unit.barsdiary.domain.global.pojo.AdvertBoardPojo
import ru.unit.barsdiary.domain.global.pojo.BirthdayPojo
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.sdk.response.GetAdvertBoardDTO
import ru.unit.barsdiary.sdk.response.GetAdvertBoardItemDTO
import ru.unit.barsdiary.sdk.response.GetBirthdaysBirthdayDTO
import ru.unit.barsdiary.sdk.response.GetBirthdaysResponseDTO
import javax.inject.Inject

class AdvertBoardTransformer @Inject constructor() : BaseTransformer<GetAdvertBoardDTO, AdvertBoardPojo> {
    override fun transform(value: GetAdvertBoardDTO): AdvertBoardPojo {
        return AdvertBoardPojo(
            value.items.mapNotNull { transform(it) }
        )
    }

    private fun transform(value: GetAdvertBoardItemDTO): AdvertBoardItemPojo? {
        val theme = value.theme
        return if (theme == null) {
            null
        } else {
            AdvertBoardItemPojo(
                theme,
                value.message,
                value.author,
                value.authorId,
                value.advertDate,
                value.school,
                value.fileUrl,
                value.id
            )
        }
    }

}