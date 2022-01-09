package ru.unit.barsdiary.sdk.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

@JsonAdapter(GetAdvertBoardDTODeserializer::class)
data class GetAdvertBoardDTO(
    val items: List<GetAdvertBoardItemDTO> = emptyList()
)

data class GetAdvertBoardItemDTO(
    @SerializedName("theme") val theme: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("author_id") val authorId: Int = 0,
    @SerializedName("advert_date") val advertDate: String?,
    @SerializedName("school") val school: String?,
    @SerializedName("file_url") val fileUrl: String?,
    @SerializedName("id") val id: Int = 0,
)

class GetAdvertBoardDTODeserializer : JsonDeserializer<GetAdvertBoardDTO> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): GetAdvertBoardDTO {
        json ?: return GetAdvertBoardDTO()

        val array = json.asJsonArray
        val result = mutableListOf<GetAdvertBoardItemDTO>()

        array?.forEach {
            val d = context?.deserialize<GetAdvertBoardItemDTO>(it, GetAdvertBoardItemDTO::class.java)

            if (d != null) {
                result.add(d)
            }
        }

        return GetAdvertBoardDTO(result)
    }
}
