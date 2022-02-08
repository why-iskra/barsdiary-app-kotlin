package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.person.pojo.EmployeePojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.sdk.response.GetSchoolInfoEmployeeDTO
import ru.unit.barsdiary.sdk.response.GetSchoolInfoResponseDTO
import javax.inject.Inject

class SchoolInfoTransformer @Inject constructor() :
    BaseTransformer<GetSchoolInfoResponseDTO, SchoolInfoPojo> {
    override fun transform(value: GetSchoolInfoResponseDTO): SchoolInfoPojo {
        return SchoolInfoPojo(
            value.address,
            value.email,
            value.name,
            value.phone,
            value.siteUrl,
            value.employees.map { transform(it) }
        )
    }

    private fun transform(value: GetSchoolInfoEmployeeDTO): EmployeePojo {
        return EmployeePojo(
            value.name,
            value.male,
            value.employerJobs
        )
    }


}