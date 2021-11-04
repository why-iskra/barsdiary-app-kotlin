package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetClassYearInfoResponseDTO(
    @SerializedName("form_master") val formMaster: String?,
    @SerializedName("form_master_male") val formMasterMale: Boolean = false,
    @SerializedName("letter") val letter: String?,
    @SerializedName("study_level") val studyLevel: Int = 1,
    @SerializedName("pupils") val classmates: List<GetClassYearInfoClassmateDTO> = emptyList(),
)

data class GetClassYearInfoClassmateDTO(
    @SerializedName("fullname") val name: String?,
    @SerializedName("male") val male: Boolean = true,
)
