package ru.unit.barsdiary.data.module.diary

import ru.unit.barsdiary.data.transformer.DiaryTransformer
import ru.unit.barsdiary.data.transformer.HomeworkTransformer
import ru.unit.barsdiary.domain.diary.DiaryService
import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import ru.unit.barsdiary.sdk.BarsWrapper
import java.time.LocalDate
import javax.inject.Inject

class DiaryServiceImpl @Inject constructor(
    private val barsWrapper: BarsWrapper,
    private val diaryTransformer: DiaryTransformer,
    private val homeworkTransformer: HomeworkTransformer,
) : DiaryService {
    override suspend fun getDiary(date: LocalDate): DiaryPojo = diaryTransformer.transform(barsWrapper.getDiary(date))

    override suspend fun getHomework(date: LocalDate): HomeworkPojo = homeworkTransformer.transform(barsWrapper.getHomework(date))

}