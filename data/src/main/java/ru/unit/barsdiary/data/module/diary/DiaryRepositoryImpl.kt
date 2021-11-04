package ru.unit.barsdiary.data.module.diary

import ru.unit.barsdiary.data.di.annotation.DiaryRamCache
import ru.unit.barsdiary.data.di.annotation.HomeworkRamCache
import ru.unit.barsdiary.data.utils.RamCache
import ru.unit.barsdiary.domain.diary.DiaryRepository
import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import java.time.LocalDate
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    @DiaryRamCache private val diaryCache: RamCache<DiaryPojo>,
    @HomeworkRamCache private val homeworkCache: RamCache<HomeworkPojo>,
) : DiaryRepository {

    override suspend fun getDiary(date: LocalDate): DiaryPojo? = diaryCache.get(date)

    override suspend fun setDiary(value: DiaryPojo, date: LocalDate) {
        diaryCache.put(value, date)
    }

    override suspend fun clearDiary() {
        diaryCache.clear()
    }

    override suspend fun getHomework(date: LocalDate): HomeworkPojo? = homeworkCache.get(date)

    override suspend fun setHomework(value: HomeworkPojo, date: LocalDate) {
        homeworkCache.put(value, date)
    }

    override suspend fun clearHomework() {
        homeworkCache.clear()
    }
}