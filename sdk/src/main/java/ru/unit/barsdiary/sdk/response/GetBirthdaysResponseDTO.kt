package ru.unit.barsdiary.sdk.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

@JsonAdapter(GetBirthdaysResponseDTODeserializer::class)
data class GetBirthdaysResponseDTO(
    val birthdays: List<GetBirthdaysBirthdayDTO> = emptyList(),
)

data class GetBirthdaysBirthdayDTO(
    @SerializedName("date") val date: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("short_name") val shortName: String?,
    @SerializedName("male") val male: Boolean = false,
)

class GetBirthdaysResponseDTODeserializer : JsonDeserializer<GetBirthdaysResponseDTO> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): GetBirthdaysResponseDTO {
        json ?: return GetBirthdaysResponseDTO()

        val array = json.asJsonArray
        val result = mutableListOf<GetBirthdaysBirthdayDTO>()

        array?.forEach {
            val d = context?.deserialize<GetBirthdaysBirthdayDTO>(it, GetBirthdaysBirthdayDTO::class.java)

            if (d != null) {
                result.add(d)
            }
        }

        return GetBirthdaysResponseDTO(result)
    }
}
