package ru.unit.barsdiary.data.utils

import ru.unit.barsdiary.data.di.annotation.*
import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo
import javax.inject.Inject

class RamCacheCleaner @Inject constructor(
    @PersonRamCache private val personCache: RamCache<PersonPojo>,
    @SchoolInfoRamCache private val schoolInfoCache: RamCache<SchoolInfoPojo>,
    @ClassInfoRamCache private val classInfoCache: RamCache<ClassInfoPojo>,
    @TotalMarksRamCache private val totalMarksCache: RamCache<TotalMarksPojo>,
    @MarksRamCache private val marksCache: RamCache<MarksPojo>,
    @AttendanceChartRamCache private val attendanceChartCache: RamCache<AttendanceChartPojo>,
    @ProgressChartRamCache private val progressChartCache: RamCache<ProgressChartPojo>,
    @BirthdaysRamCache private val birthdaysCache: RamCache<BirthdaysPojo>,
    @InBoxRamCache private val inBoxCache: RamCache<BoxPojo>,
    @OutBoxRamCache private val outBoxCache: RamCache<BoxPojo>,
    @InBoxCountRamCache private val inBoxCountCache: RamCache<Int>,
    @DiaryRamCache private val diaryCache: RamCache<DiaryPojo>,
    @HomeworkRamCache private val homeworkCache: RamCache<HomeworkPojo>,
) {
    suspend fun clean() {
        personCache.clear()
        schoolInfoCache.clear()
        classInfoCache.clear()
        totalMarksCache.clear()
        marksCache.clear()
        attendanceChartCache.clear()
        progressChartCache.clear()
        birthdaysCache.clear()
        inBoxCache.clear()
        outBoxCache.clear()
        inBoxCountCache.clear()
        diaryCache.clear()
        homeworkCache.clear()
    }
}