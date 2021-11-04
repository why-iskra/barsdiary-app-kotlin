package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetSummaryMarksResponseDTO(
    @SerializedName("discipline_marks") val disciplineMarks: List<GetSummaryMarksDisciplineMarksDTO> = emptyList(),
    @SerializedName("dates") val dates: List<String> = emptyList(),
    @SerializedName("subperiod") val subPeriod: GetSummaryMarksSubPeriodDTO?,
)

data class GetSummaryMarksDisciplineMarksDTO(
    @SerializedName("average_mark") val averageMark: String?,
    @SerializedName("marks") val marks: List<GetSummaryMarksMarkDTO> = emptyList(),
    @SerializedName("discipline") val discipline: String?,
)

data class GetSummaryMarksMarkDTO(
    @SerializedName("mark") val mark: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("date") val date: String?,
)

data class GetSummaryMarksSubPeriodDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("code") val code: String?,
)
