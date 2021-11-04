package ru.unit.barsdiary.domain.diary

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import java.time.LocalDate
import javax.inject.Inject

interface DiaryUseCase {
    suspend fun getDiary(date: LocalDate): Pair<DiaryPojo, HomeworkPojo>
    suspend fun clear()
}

class DiaryUseCaseImpl @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val diaryService: DiaryService,
) : DiaryUseCase {

    override suspend fun getDiary(date: LocalDate): Pair<DiaryPojo, HomeworkPojo> = coroutineScope {
        val responseDiary = async { diaryRepository.getDiary(date) }
        val responseHomework = async { diaryRepository.getHomework(date) }

        val diary = responseDiary.await() ?: diaryService.getDiary(date).apply { diaryRepository.setDiary(this, date) }
        val homework = responseHomework.await() ?: diaryService.getHomework(date).apply { diaryRepository.setHomework(this, date) }

        return@coroutineScope diary to homework
    }

    override suspend fun clear() {
        diaryRepository.clearDiary()
        diaryRepository.clearHomework()
    }

}