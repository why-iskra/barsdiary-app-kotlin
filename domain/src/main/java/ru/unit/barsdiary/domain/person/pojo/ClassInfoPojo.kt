package ru.unit.barsdiary.domain.person.pojo

data class ClassInfoPojo(
    val formMaster: String?,
    val formMasterMale: Boolean = false,
    val letter: String?,
    val studyLevel: Int = 1,
    val classmates: List<ClassmatePojo> = emptyList(),
)
