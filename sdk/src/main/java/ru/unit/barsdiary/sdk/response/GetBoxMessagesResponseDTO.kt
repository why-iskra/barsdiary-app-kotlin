package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetBoxMessagesResponseDTO(
    @SerializedName("count") val count: Int,
    @SerializedName("new_count") val newCount: Int,
    @SerializedName("items") val items: List<GetBoxMessageDTO>?,
)

data class GetBoxMessageDTO(
    @SerializedName("date") val date: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("is_new") val isNew: Boolean,
    @SerializedName("sender_fullname") val senderFullName: String?,
    @SerializedName("short_text") val shortText: String?,
    @SerializedName("subject") val subject: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("attachments") val attachments: List<GetBoxMessageAttachmentDTO>?,
    @SerializedName("receivers") val receivers: List<GetBoxMessageReceiverDTO>?,
)

data class GetBoxMessageAttachmentDTO(
    @SerializedName("download_link") val downloadLink: String?,
    @SerializedName("original_name") val originalName: String?,
)

data class GetBoxMessageReceiverDTO(
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("profile_id") val profileId: Int,
)
