package ru.unit.barsdiary.domain.diary.pojo

data class HomeworkDayPojo(
    val date: String,
    val homework: List<HomeworkLessonPojo>,
)
