package ru.unit.barsdiary.data.module.person

import ru.unit.barsdiary.data.transformer.ClassInfoTransformer
import ru.unit.barsdiary.data.transformer.PersonTransformer
import ru.unit.barsdiary.data.transformer.SchoolInfoTransformer
import ru.unit.barsdiary.data.transformer.TotalMarksTransformer
import ru.unit.barsdiary.domain.person.PersonService
import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo
import ru.unit.barsdiary.sdk.Engine
import javax.inject.Inject

class PersonServiceImpl @Inject constructor(
    private val engine: Engine,
    private val personTransformer: PersonTransformer,
    private val schoolInfoTransformer: SchoolInfoTransformer,
    private val classInfoTransformer: ClassInfoTransformer,
    private val totalMarksTransformer: TotalMarksTransformer,
) : PersonService {
    override suspend fun getPerson(): PersonPojo = personTransformer.transform(engine.api { getPersonData() })
    override suspend fun getSchoolInfo(): SchoolInfoPojo = schoolInfoTransformer.transform(engine.api { getSchoolInfo() })
    override suspend fun getClassInfo(): ClassInfoPojo = classInfoTransformer.transform(engine.api { getClassYearInfo() })
    override suspend fun getTotalMarks(): TotalMarksPojo = totalMarksTransformer.transform(engine.api { getTotalMarks() })
}