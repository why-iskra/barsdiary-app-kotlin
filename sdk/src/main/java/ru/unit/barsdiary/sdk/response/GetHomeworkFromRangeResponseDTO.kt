package ru.unit.barsdiary.sdk.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type


@JsonAdapter(GetHomeworkFromRangeResponseDTODeserializer::class)
data class GetHomeworkFromRangeResponseDTO(
    val days: List<GetHomeworkFromRangeDayDTO> = emptyList(),
)

data class GetHomeworkFromRangeDayDTO(
    @SerializedName("date") val date: String?,
    @SerializedName("homeworks") val homeworks: List<GetHomeworkFromRangeHomeworkDTO> = emptyList(),
)

// todo: add individual homework
data class GetHomeworkFromRangeHomeworkDTO(
    @SerializedName("homework") val homework: String?,
    @SerializedName("teacher") val teacher: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("materials") val materials: List<GetHomeworkFromRangeMaterialDTO> = emptyList(),
    @SerializedName("theme") val theme: String?,
    @SerializedName("schedulelessontype") val scheduleLessonType: String?,
    @SerializedName("nextMaterials") val nextMaterials: List<GetHomeworkFromRangeMaterialDTO> = emptyList(),
    @SerializedName("nextHomework") val nextHomework: String?,
    @SerializedName("individualHomeworks") val individualHomeworks: List<GetHomeworkFromRangeIndividualHomeworkDTO> = emptyList(),
    @SerializedName("nextIndividualHomeworks") val nextIndividualHomeworks: List<GetHomeworkFromRangeIndividualHomeworkDTO> = emptyList(),
    @SerializedName("discipline") val discipline: String?,
)

data class GetHomeworkFromRangeMaterialDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?,
)

data class GetHomeworkFromRangeIndividualHomeworkDTO(
    @SerializedName("description") val description: String?,
    @SerializedName("document") val document: GetHomeworkFromRangeMaterialDTO?,
    @SerializedName("lesson_id") val lessonId: String?,
)

class GetHomeworkFromRangeResponseDTODeserializer : JsonDeserializer<GetHomeworkFromRangeResponseDTO> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): GetHomeworkFromRangeResponseDTO {
        json ?: return GetHomeworkFromRangeResponseDTO()

        val array = json.asJsonArray
        val result = mutableListOf<GetHomeworkFromRangeDayDTO>()

        array?.forEach {
            val d = context?.deserialize<GetHomeworkFromRangeDayDTO>(it, GetHomeworkFromRangeDayDTO::class.java)

            if (d != null) {
                result.add(d)
            }
        }

        return GetHomeworkFromRangeResponseDTO(result)
    }
}
