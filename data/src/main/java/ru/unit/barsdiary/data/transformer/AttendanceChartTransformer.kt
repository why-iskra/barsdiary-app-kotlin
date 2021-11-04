package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.sdk.response.GetAttendanceChartResponseDTO
import javax.inject.Inject

class AttendanceChartTransformer @Inject constructor() : BaseTransformer<GetAttendanceChartResponseDTO?, AttendanceChartPojo?> {
    override fun transform(value: GetAttendanceChartResponseDTO?): AttendanceChartPojo? {
        return if (value == null) {
            null
        } else {
            AttendanceChartPojo(
                value.present,
                value.total,
                value.absentBad,
                value.ill,
                value.absentGood,
                value.absent
            )
        }
    }

}