package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetPersonDataResponseDTO(
    @SerializedName("auth_user_profile_id") val authUserProfileId: Int = 0,

    @SerializedName("selected_pupil_is_male") val selectedPupilIsMale: Boolean = false,
    @SerializedName("selected_pupil_id") val selectedPupilId: Int = 0,
    @SerializedName("selected_pupil_school") val selectedPupilSchool: String?,
    @SerializedName("selected_pupil_name") val selectedPupilName: String?,
    @SerializedName("selected_pupil_classyear") val selectedPupilClassYear: String?,
    @SerializedName("selected_pupil_ava_url") val selectedPupilAvaUrl: String?,

    @SerializedName("user_fullname") val userFullName: String?,
    @SerializedName("user_desc") val userDesc: String?,
    @SerializedName("user_is_male") val userIsMale: Boolean = false,
    @SerializedName("user_email") val userEmail: String?,
    @SerializedName("user_has_ava") val userHasAva: Boolean = false,
    @SerializedName("user_ava_url") val userAvaUrl: String?,

    @SerializedName("indicators") val indicators: List<GetPersonDataIndicatorDTO> = emptyList(),
    @SerializedName("children_persons") val childrenPersons: List<GetPersonDataChildPersonDTO> = emptyList(),
)

data class GetPersonDataIndicatorDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("value") val value: String?,
    @SerializedName("css") val css: String?,
)

data class GetPersonDataChildPersonDTO(
    @SerializedName("school") val school: String?,
    @SerializedName("is_male") val isMale: Boolean = false,
    @SerializedName("classyear") val classYear: String?,
    @SerializedName("person_id") val personId: Int = 0,
    @SerializedName("fullname") val fullName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("ava_url") val avaUrl: String?,
)