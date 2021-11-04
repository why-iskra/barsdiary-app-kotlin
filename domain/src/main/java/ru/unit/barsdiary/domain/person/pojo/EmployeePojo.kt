package ru.unit.barsdiary.domain.person.pojo

data class EmployeePojo(
    val name: String?,
    val male: Boolean,
    val employerJobs: List<String>,
)
