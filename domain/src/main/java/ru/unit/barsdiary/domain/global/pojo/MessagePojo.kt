package ru.unit.barsdiary.domain.global.pojo

data class MessagePojo(
    val date: String?,
    val id: Int,
    val isNew: Boolean,
    val senderFullName: String?,
    val shortText: String?,
    val subject: String?,
    val text: String?,
    val attachments: List<AttachmentPojo>,
    val receivers: List<ReceiverPojo>,
) {
    var markRead: Boolean = false
}