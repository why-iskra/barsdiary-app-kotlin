package ru.unit.barsdiary.domain.diary.pojo

data class DiaryLessonPojo(
    val homework: String?,
    val teacher: String?,
    val remarks: String?,
    val studyTimeName: String?,
    val materials: List<MaterialPojo>,
    val theme: String?,
    val date: String?,
    val mark: String?,
    val timeEnd: String?,
    val timeBegin: String?,
    val scheduleLessonType: String?,
    val index: Int,
    val office: String?,
    val attendance: String?,
    val discipline: String?,
    val comment: String?,
)
