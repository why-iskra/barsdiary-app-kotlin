package ru.unit.barsdiary.data.module.diary

import ru.unit.barsdiary.data.di.annotation.WebDateFormatter
import ru.unit.barsdiary.data.transformer.DiaryTransformer
import ru.unit.barsdiary.data.transformer.HomeworkTransformer
import ru.unit.barsdiary.domain.diary.DiaryService
import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DiaryServiceImpl @Inject constructor(
    private val engine: BarsDiaryEngine,
    @WebDateFormatter private val webDateFormatter: DateTimeFormatter,

    private val diaryTransformer: DiaryTransformer,
    private val homeworkTransformer: HomeworkTransformer,
) : DiaryService {
    override suspend fun getDiary(date: LocalDate): DiaryPojo =
        diaryTransformer.transform(engine.api { getDiary(webDateFormatter.format(date), true) })

    override suspend fun getHomework(date: LocalDate): HomeworkPojo =
        homeworkTransformer.transform(engine.api { getHomework(webDateFormatter.format(date)) })

}