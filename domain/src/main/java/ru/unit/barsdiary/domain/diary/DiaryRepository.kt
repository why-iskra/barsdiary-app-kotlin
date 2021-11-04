package ru.unit.barsdiary.domain.diary

import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import java.time.LocalDate

interface DiaryRepository {
    suspend fun getDiary(date: LocalDate): DiaryPojo?
    suspend fun setDiary(value: DiaryPojo, date: LocalDate)
    suspend fun clearDiary()

    suspend fun getHomework(date: LocalDate): HomeworkPojo?
    suspend fun setHomework(value: HomeworkPojo, date: LocalDate)
    suspend fun clearHomework()
}