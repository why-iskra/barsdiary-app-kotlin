package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetVisualizationDataResponseDTO(
    @SerializedName("period_begin") val periodBegin: String?,
    @SerializedName("period_end") val periodEnd: String?,
    @SerializedName("student_id_param_name") val studentIdParamName: String?,
    @SerializedName("charts_urls") val chartsUrls: GetVisualizationDataChartsUrlsDTO?,
    @SerializedName("disciplines") val disciplines: List<GetVisualizationDataDisciplinesDTO> = emptyList(),
    @SerializedName("pupil_id") val pupilId: Int = 0,
)

data class GetVisualizationDataDisciplinesDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("id") val id: Int = 0,
)

data class GetVisualizationDataChartsUrlsDTO(
    @SerializedName("attendance") val attendance: String?,
    @SerializedName("progress") val progress: String?,
)