package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetTotalMarksResponseDTO(
    @SerializedName("discipline_marks") val disciplineMarks: List<GetTotalMarksDisciplineMarksDTO> = emptyList(),
    @SerializedName("subperiods") val subperiods: List<GetTotalMarksSubperiodDTO> = emptyList(),
)

data class GetTotalMarksSubperiodDTO(
    @SerializedName("code") val code: String?,
    @SerializedName("name") val name: String?,
)

data class GetTotalMarksDisciplineMarksDTO(
    @SerializedName("discipline") val discipline: String?,
    @SerializedName("period_marks") val periodMarks: List<GetTotalMarksPeriodMarkDTO> = emptyList(),
)


data class GetTotalMarksPeriodMarkDTO(
    @SerializedName("subperiod_code") val subperiodCode: String?,
    @SerializedName("mark") val mark: String?,
)
