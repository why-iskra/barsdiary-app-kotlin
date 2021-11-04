package ru.unit.barsdiary.domain.person.pojo

data class PersonPojo(
    val authUserProfileId: Int,

    val selectedPupilIsMale: Boolean,
    val selectedPupilId: Int,
    val selectedPupilSchool: String?,
    val selectedPupilName: String?,
    val selectedPupilClassYear: String?,
    val selectedPupilAvaUrl: String?,

    val userFullName: String?,
    val userDesc: String?,
    val userIsMale: Boolean,
    val userEmail: String?,
    val userHasAva: Boolean,
    val userAvaUrl: String?,

    val indicators: List<PersonIndicatorPojo>,
    val childrenPersons: List<PersonChildPojo>,
)