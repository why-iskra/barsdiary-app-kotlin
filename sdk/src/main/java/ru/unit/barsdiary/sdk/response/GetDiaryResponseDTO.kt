package ru.unit.barsdiary.sdk.response

import com.google.gson.annotations.SerializedName

data class GetDiaryResponseDTO(
    @SerializedName("days") val days: List<GetDiaryDayDTO> = emptyList(),
)

data class GetDiaryDayDTO(
    @SerializedName("date") val date: String?,
    @SerializedName("lessons") val lessons: List<GetDiaryLessonDTO>? = emptyList(),
    @SerializedName("is_vacation") val isVacation: Boolean? = null,
    @SerializedName("is_holiday") val isHoliday: Boolean? = null,
    @SerializedName("is_weekend") val isWeekend: Boolean? = null,
)

data class GetDiaryLessonDTO(
    @SerializedName("homework") val homework: String?,
    @SerializedName("teacher") val teacher: String?,
    @SerializedName("remarks") val remarks: String?,
    @SerializedName("study_time_name") val studyTimeName: String?,
    @SerializedName("materials") val materials: List<GetDiaryMaterialDTO> = emptyList(),
    @SerializedName("theme") val theme: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("mark") val mark: String?,
    @SerializedName("time_end") val timeEnd: String?,
    @SerializedName("time_begin") val timeBegin: String?,
    @SerializedName("schedulelessontype") val scheduleLessonType: String?,
    @SerializedName("index") val index: Int = 0,
    @SerializedName("office") val office: String?,
    @SerializedName("ind_homework_exists") val indHomeworkExists: Boolean = false,
    @SerializedName("attendance") val attendance: String?,
    @SerializedName("discipline") val discipline: String?,
    @SerializedName("comment") val comment: String?,
)

data class GetDiaryMaterialDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?,
)