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
import ru.unit.barsdiary.sdk.BarsWrapper
import javax.inject.Inject

class PersonServiceImpl @Inject constructor(
    private val barsWrapper: BarsWrapper,
    private val personTransformer: PersonTransformer,
    private val schoolInfoTransformer: SchoolInfoTransformer,
    private val classInfoTransformer: ClassInfoTransformer,
    private val totalMarksTransformer: TotalMarksTransformer,
) : PersonService {
    override suspend fun getPerson(): PersonPojo = personTransformer.transform(barsWrapper.getPersonData())
    override suspend fun getSchoolInfo(): SchoolInfoPojo = schoolInfoTransformer.transform(barsWrapper.getSchoolInfo())
    override suspend fun getClassInfo(): ClassInfoPojo = classInfoTransformer.transform(barsWrapper.getClassYearInfo())
    override suspend fun getTotalMarks(): TotalMarksPojo = totalMarksTransformer.transform(barsWrapper.getTotalMarks())
}