package ru.unit.barsdiary.domain.diary.pojo

data class DiaryDayPojo(
    val date: String,
    val lessons: List<DiaryLessonPojo>?,
    val isVacation: Boolean,
    val isHoliday: Boolean,
    val isWeekend: Boolean,
)
