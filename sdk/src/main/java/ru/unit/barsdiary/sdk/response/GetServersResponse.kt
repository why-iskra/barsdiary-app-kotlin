package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetServersResponse(
    @SerializedName("data") val data: List<GetServersItem>? = emptyList(),
)

data class GetServersItem(
    @SerializedName("url") val url: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("extended_version") val extendedVersion: Boolean = false,
)
