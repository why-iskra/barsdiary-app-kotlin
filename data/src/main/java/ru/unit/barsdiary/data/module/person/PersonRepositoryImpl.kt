package ru.unit.barsdiary.data.module.person

import ru.unit.barsdiary.data.di.annotation.ClassInfoRamCache
import ru.unit.barsdiary.data.di.annotation.PersonRamCache
import ru.unit.barsdiary.data.di.annotation.SchoolInfoRamCache
import ru.unit.barsdiary.data.di.annotation.TotalMarksRamCache
import ru.unit.barsdiary.data.utils.RamCache
import ru.unit.barsdiary.domain.person.PersonRepository
import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    @PersonRamCache private val personCache: RamCache<PersonPojo>,
    @SchoolInfoRamCache private val schoolInfoCache: RamCache<SchoolInfoPojo>,
    @ClassInfoRamCache private val classInfoCache: RamCache<ClassInfoPojo>,
    @TotalMarksRamCache private val totalMarksCache: RamCache<TotalMarksPojo>,
) : PersonRepository {

    override suspend fun getPerson(): PersonPojo? = personCache.get()

    override suspend fun setPerson(value: PersonPojo) {
        personCache.put(value)
    }

    override suspend fun clearPerson() {
        personCache.clear()
    }

    override suspend fun getSchoolInfo(): SchoolInfoPojo? = schoolInfoCache.get()

    override suspend fun setSchoolInfo(value: SchoolInfoPojo) {
        schoolInfoCache.put(value)
    }

    override suspend fun clearSchoolInfo() {
        schoolInfoCache.clear()
    }

    override suspend fun getClassInfo(): ClassInfoPojo? = classInfoCache.get()

    override suspend fun setClassInfo(value: ClassInfoPojo) {
        classInfoCache.put(value)
    }

    override suspend fun clearClassInfo() {
        classInfoCache.clear()
    }

    override suspend fun getTotalMarks(): TotalMarksPojo? = totalMarksCache.get()

    override suspend fun setTotalMarks(value: TotalMarksPojo) {
        totalMarksCache.put(value)
    }

    override suspend fun clearTotalMarks() {
        totalMarksCache.clear()
    }
}