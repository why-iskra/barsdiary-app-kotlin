package ru.unit.barsdiary.domain.person.pojo

data class TotalMarksPojo(
    val disciplineMarks: List<TotalMarksDisciplinePojo>,
    val subperiods: List<TotalMarksSubperiodPojo>,
)
