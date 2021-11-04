package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetSchoolInfoResponseDTO(
    @SerializedName("address") val address: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("site_url") val siteUrl: String?,
    @SerializedName("employees") val employees: List<GetSchoolInfoEmployeeDTO> = emptyList(),
)

data class GetSchoolInfoEmployeeDTO(
    @SerializedName("fullname") val name: String?,
    @SerializedName("male") val male: Boolean = true,
    @SerializedName("employer_jobs") val employerJobs: List<String> = emptyList(),
)
