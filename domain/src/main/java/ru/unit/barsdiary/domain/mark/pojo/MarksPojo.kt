package ru.unit.barsdiary.domain.mark.pojo

data class MarksPojo(
    val disciplineMarks: List<DisciplineMarksPojo>,
    val dates: List<String>,
    val subPeriod: MarksSubPeriodPojo?,
)