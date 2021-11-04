package ru.unit.barsdiary.domain.diary

import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import java.time.LocalDate

interface DiaryService {
    suspend fun getDiary(date: LocalDate): DiaryPojo
    suspend fun getHomework(date: LocalDate): HomeworkPojo
}