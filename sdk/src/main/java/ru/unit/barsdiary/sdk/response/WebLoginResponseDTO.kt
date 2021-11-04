package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class WebLoginResponseDTO(
    @SerializedName("redirect") val redirect: String?,
    @SerializedName("success") val success: Boolean = false,
)
