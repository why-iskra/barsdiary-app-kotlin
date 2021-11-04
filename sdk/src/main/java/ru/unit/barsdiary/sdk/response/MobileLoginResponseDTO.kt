package ru.unit.barsdiary.sdk.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class MobileLoginResponseDTO(
    @SerializedName("childs") val children: List<MobileLoginChildDTO> = emptyList(),
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("profile_id") val profileId: Int = 0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("type") val type: String?,
    @SerializedName("fio") val fio: String?,
)

@JsonAdapter(MobileLoginResponseDTODeserializer::class)
data class MobileLoginChildDTO(
    val id: Int = 0,
    val name: String?,
    val school: String?,
)

class MobileLoginResponseDTODeserializer : JsonDeserializer<MobileLoginChildDTO> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): MobileLoginChildDTO {
        val array = json?.asJsonArray
        val id = array?.get(0)?.asInt ?: 0
        val name = array?.get(1)?.asString
        val school = array?.get(2)?.asString

        return MobileLoginChildDTO(id, name, school)
    }
}
