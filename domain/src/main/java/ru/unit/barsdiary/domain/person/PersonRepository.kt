package ru.unit.barsdiary.domain.person

import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo

interface PersonRepository {
    suspend fun getPerson(): PersonPojo?
    suspend fun setPerson(value: PersonPojo)
    suspend fun clearPerson()

    suspend fun getSchoolInfo(): SchoolInfoPojo?
    suspend fun setSchoolInfo(value: SchoolInfoPojo)
    suspend fun clearSchoolInfo()

    suspend fun getClassInfo(): ClassInfoPojo?
    suspend fun setClassInfo(value: ClassInfoPojo)
    suspend fun clearClassInfo()

    suspend fun getTotalMarks(): TotalMarksPojo?
    suspend fun setTotalMarks(value: TotalMarksPojo)
    suspend fun clearTotalMarks()
}