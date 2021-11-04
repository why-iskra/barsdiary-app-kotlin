package ru.unit.barsdiary.domain.person

import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo

interface PersonService {
    suspend fun getPerson(): PersonPojo
    suspend fun getSchoolInfo(): SchoolInfoPojo
    suspend fun getClassInfo(): ClassInfoPojo
    suspend fun getTotalMarks(): TotalMarksPojo
}