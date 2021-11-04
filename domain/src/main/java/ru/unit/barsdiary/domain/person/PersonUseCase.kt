package ru.unit.barsdiary.domain.person

import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo
import javax.inject.Inject

interface PersonUseCase {
    suspend fun getPerson(): PersonPojo
    suspend fun getSchoolInfo(): SchoolInfoPojo
    suspend fun getClassInfo(): ClassInfoPojo
    suspend fun getTotalMarks(): TotalMarksPojo
    suspend fun clearPerson()
    suspend fun clearSchoolInfo()
    suspend fun clearClassInfo()
    suspend fun clearTotalMarks()
    suspend fun clear()
}

class PersonUseCaseImpl @Inject constructor(
    private val personRepository: PersonRepository,
    private val personService: PersonService,
) : PersonUseCase {

    override suspend fun getPerson(): PersonPojo =
        personRepository.getPerson() ?: personService.getPerson().apply { personRepository.setPerson(this) }

    override suspend fun getSchoolInfo(): SchoolInfoPojo =
        personRepository.getSchoolInfo() ?: personService.getSchoolInfo().apply { personRepository.setSchoolInfo(this) }

    override suspend fun getClassInfo(): ClassInfoPojo =
        personRepository.getClassInfo() ?: personService.getClassInfo().apply { personRepository.setClassInfo(this) }

    override suspend fun getTotalMarks(): TotalMarksPojo =
        personRepository.getTotalMarks() ?: personService.getTotalMarks().apply { personRepository.setTotalMarks(this) }

    override suspend fun clearPerson() {
        personRepository.clearPerson()
    }

    override suspend fun clearSchoolInfo() {
        personRepository.clearSchoolInfo()
    }

    override suspend fun clearClassInfo() {
        personRepository.clearClassInfo()
    }

    override suspend fun clearTotalMarks() {
        personRepository.clearTotalMarks()
    }

    override suspend fun clear() {
        clearPerson()
        clearSchoolInfo()
        clearClassInfo()
        clearTotalMarks()
    }

}