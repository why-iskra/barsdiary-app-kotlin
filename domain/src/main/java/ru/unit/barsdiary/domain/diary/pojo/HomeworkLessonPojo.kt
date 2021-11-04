package ru.unit.barsdiary.domain.diary.pojo

data class HomeworkLessonPojo(
    val homework: String?,
    val teacher: String?,
    val date: String?,
    val materials: List<MaterialPojo>,
    val theme: String?,
    val scheduleLessonType: String?,
    val individualHomeworks: List<HomeworkIndividualPojo>,
    val discipline: String?,
)
