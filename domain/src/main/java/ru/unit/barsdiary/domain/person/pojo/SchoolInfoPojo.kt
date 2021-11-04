package ru.unit.barsdiary.domain.person.pojo

data class SchoolInfoPojo(
    val address: String?,
    val email: String?,
    val name: String?,
    val phone: String?,
    val siteUrl: String?,
    val employees: List<EmployeePojo>,
)


