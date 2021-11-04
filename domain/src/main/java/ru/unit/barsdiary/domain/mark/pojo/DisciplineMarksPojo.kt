package ru.unit.barsdiary.domain.mark.pojo

data class DisciplineMarksPojo(
    val averageMark: String?,
    val marks: List<MarkPojo>,
    val discipline: String?,
)
