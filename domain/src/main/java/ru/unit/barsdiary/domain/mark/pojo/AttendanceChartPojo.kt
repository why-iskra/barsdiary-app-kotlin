package ru.unit.barsdiary.domain.mark.pojo

data class AttendanceChartPojo(
    val present: Int,
    val total: Int,
    val absentBad: Int,
    val ill: Int,
    val absentGood: Int,
    val absent: Int,
)
