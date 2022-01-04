package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetUserProfilesResponseDTO(
    @SerializedName("count") val count: Int = 0,
    @SerializedName("items") val items: List<GetUserProfileDTO>? = emptyList(),
)

data class GetUserProfileDTO(
    @SerializedName("id") val id: String?,
    @SerializedName("profile_id") val profileId: Int = 0,
    @SerializedName("fullname") val fullName: String?,

    @SerializedName("class_name") val className: String?,
    @SerializedName("child_fullname") val childFullName: String?,
)