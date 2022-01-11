package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetClassHoursDTO(
    @SerializedName("date") val date: String?,
    @SerializedName("begin") val begin: String?,
    @SerializedName("end") val end: String?,
    @SerializedName("place") val place: String?,
    @SerializedName("theme") val theme: String?
)
