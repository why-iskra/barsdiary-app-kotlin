package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetAttendanceChartResponseDTO(
    @SerializedName("present") val present: Int = 0,
    @SerializedName("total") val total: Int = 0,
    @SerializedName("absent_bad") val absentBad: Int = 0,
    @SerializedName("ill") val ill: Int = 0,
    @SerializedName("absent_good") val absentGood: Int = 0,
    @SerializedName("absent") val absent: Int = 0,
)