package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.person.pojo.PersonChildPojo
import ru.unit.barsdiary.domain.person.pojo.PersonIndicatorPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.sdk.response.GetPersonDataChildPersonDTO
import ru.unit.barsdiary.sdk.response.GetPersonDataIndicatorDTO
import ru.unit.barsdiary.sdk.response.GetPersonDataResponseDTO
import javax.inject.Inject

class PersonTransformer @Inject constructor() : BaseTransformer<GetPersonDataResponseDTO, PersonPojo> {
    override fun transform(value: GetPersonDataResponseDTO): PersonPojo {
        return PersonPojo(
            value.authUserProfileId,
            value.selectedPupilIsMale,
            value.selectedPupilId,
            value.selectedPupilSchool,
            value.selectedPupilName,
            value.selectedPupilClassYear,
            value.selectedPupilAvaUrl,
            value.userFullName,
            value.userDesc,
            value.userIsMale,
            value.userEmail,
            value.userHasAva,
            value.userAvaUrl,
            value.indicators.mapNotNull { transform(it) },
            value.childrenPersons.map { transform(it) }
        )
    }

    private fun transform(value: GetPersonDataIndicatorDTO): PersonIndicatorPojo? {
        val indicatorValue = value.value
        return if (indicatorValue.isNullOrEmpty()) {
            null
        } else {
            PersonIndicatorPojo(
                value.name,
                indicatorValue
            )
        }
    }

    private fun transform(value: GetPersonDataChildPersonDTO): PersonChildPojo {
        return PersonChildPojo(
            value.school,
            value.isMale,
            value.classYear,
            value.personId,
            value.fullName,
            value.email,
            value.avaUrl
        )
    }
}