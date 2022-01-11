package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetMeetingDTO(
    @SerializedName("date") val date: String?,
    @SerializedName("begin") val begin: String?,
    @SerializedName("end") val end: String?,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("office") val office: String?,
    @SerializedName("protocol") val protocol: String?
)
