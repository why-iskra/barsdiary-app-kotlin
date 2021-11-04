package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetProgressChartResponseDTO(
    @SerializedName("subject") val subject: String?,
    @SerializedName("categories") val categories: List<Int> = emptyList(),
    @SerializedName("dates") val dates: List<String> = emptyList(),
    @SerializedName("series") val series: List<GetProgressChartSeriesDTO> = emptyList(),
)

data class GetProgressChartSeriesDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("data") val data: List<Double> = emptyList(),
)
