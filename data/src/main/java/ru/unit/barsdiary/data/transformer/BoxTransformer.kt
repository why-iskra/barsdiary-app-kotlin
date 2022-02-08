package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.global.pojo.AttachmentPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.domain.global.pojo.ReceiverPojo
import ru.unit.barsdiary.sdk.response.GetBoxMessageAttachmentDTO
import ru.unit.barsdiary.sdk.response.GetBoxMessageDTO
import ru.unit.barsdiary.sdk.response.GetBoxMessageReceiverDTO
import ru.unit.barsdiary.sdk.response.GetBoxMessagesResponseDTO
import javax.inject.Inject

class BoxTransformer @Inject constructor() : BaseTransformer<GetBoxMessagesResponseDTO, BoxPojo> {
    override fun transform(value: GetBoxMessagesResponseDTO): BoxPojo {
        return BoxPojo(
            value.count,
            value.newCount,
            value.items?.map { transform(it) } ?: emptyList()
        )
    }

    private fun transform(value: GetBoxMessageDTO): MessagePojo {
        return MessagePojo(
            value.date,
            value.id,
            value.isNew,
            value.senderFullName,
            value.shortText,
            value.subject,
            value.text,
            value.attachments?.mapNotNull { transform(it) } ?: emptyList(),
            value.receivers?.mapNotNull { transform(it) } ?: emptyList(),
        )
    }

    private fun transform(value: GetBoxMessageAttachmentDTO): AttachmentPojo? {
        val downloadLink = value.downloadLink
        val originalName = value.originalName

        return if (!downloadLink.isNullOrBlank() && !originalName.isNullOrBlank()) {
            AttachmentPojo(
                downloadLink,
                originalName
            )
        } else {
            null
        }
    }

    private fun transform(value: GetBoxMessageReceiverDTO): ReceiverPojo? {
        val fullname = value.fullname
        return if (!fullname.isNullOrBlank()) {
            ReceiverPojo(
                fullname,
                value.id,
                value.profileId,
            )
        } else {
            null
        }
    }

}