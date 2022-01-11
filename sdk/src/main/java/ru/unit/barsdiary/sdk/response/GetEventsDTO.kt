package ru.unit.barsdiary.sdk.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

@JsonAdapter(GetEventsDTODeserializer::class)
data class GetEventsDTO(
    val items: List<GetEventsItemDTO> = emptyList()
)

data class GetEventsItemDTO(
    @SerializedName("date") val date: String?,
    @SerializedName("date_str") val dateStr: String?,
    @SerializedName("theme") val theme: String?,
)

class GetEventsDTODeserializer : JsonDeserializer<GetEventsDTO> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): GetEventsDTO {
        json ?: return GetEventsDTO()

        val array = json.asJsonArray
        val result = mutableListOf<GetEventsItemDTO>()

        array?.forEach {
            val d = context?.deserialize<GetEventsItemDTO>(it, GetEventsItemDTO::class.java)

            if (d != null) {
                result.add(d)
            }
        }

        return GetEventsDTO(result)
    }
}
