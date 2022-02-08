package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartSeriesPojo
import ru.unit.barsdiary.sdk.response.GetProgressChartResponseDTO
import ru.unit.barsdiary.sdk.response.GetProgressChartSeriesDTO
import javax.inject.Inject

class ProgressChartTransformer @Inject constructor() :
    BaseTransformer<GetProgressChartResponseDTO?, ProgressChartPojo?> {
    override fun transform(value: GetProgressChartResponseDTO?): ProgressChartPojo? {
        return if (value == null) {
            null
        } else {
            ProgressChartPojo(
                value.subject,
                value.categories,
                value.dates,
                value.series.map { transform(it) }
            )
        }
    }

    private fun transform(value: GetProgressChartSeriesDTO): ProgressChartSeriesPojo {
        return ProgressChartSeriesPojo(
            value.name,
            value.data
        )
    }
}