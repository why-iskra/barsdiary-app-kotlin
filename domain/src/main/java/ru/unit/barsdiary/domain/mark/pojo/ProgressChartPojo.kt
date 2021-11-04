package ru.unit.barsdiary.domain.mark.pojo

data class ProgressChartPojo(
    val subject: String?,
    val categories: List<Int>,
    val dates: List<String>,
    val series: List<ProgressChartSeriesPojo>,
)
