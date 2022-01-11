package ru.unit.barsdiary.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.unit.barsdiary.data.di.annotation.*
import ru.unit.barsdiary.data.utils.RamCache
import ru.unit.barsdiary.domain.diary.pojo.DiaryPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkPojo
import ru.unit.barsdiary.domain.global.pojo.*
import ru.unit.barsdiary.domain.mark.pojo.AttendanceChartPojo
import ru.unit.barsdiary.domain.mark.pojo.MarksPojo
import ru.unit.barsdiary.domain.mark.pojo.ProgressChartPojo
import ru.unit.barsdiary.domain.person.pojo.ClassInfoPojo
import ru.unit.barsdiary.domain.person.pojo.PersonPojo
import ru.unit.barsdiary.domain.person.pojo.SchoolInfoPojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RamCacheModule {

    @Provides
    @Singleton
    @PersonRamCache
    fun providePersonCache(): RamCache<PersonPojo> = RamCache()

    @Provides
    @Singleton
    @SchoolInfoRamCache
    fun provideSchoolInfoCache(): RamCache<SchoolInfoPojo> = RamCache()

    @Provides
    @Singleton
    @ClassInfoRamCache
    fun provideClassInfoCache(): RamCache<ClassInfoPojo> = RamCache()

    @Provides
    @Singleton
    @TotalMarksRamCache
    fun provideTotalMarksCache(): RamCache<TotalMarksPojo> = RamCache()


    @Provides
    @Singleton
    @MarksRamCache
    fun provideMarksCache(): RamCache<MarksPojo> = RamCache()

    @Provides
    @Singleton
    @AttendanceChartRamCache
    fun provideAttendanceChartCache(): RamCache<AttendanceChartPojo> = RamCache()

    @Provides
    @Singleton
    @ProgressChartRamCache
    fun provideProgressChartCache(): RamCache<ProgressChartPojo> = RamCache()


    @Provides
    @Singleton
    @BirthdaysRamCache
    fun provideBirthdaysCache(): RamCache<BirthdaysPojo> = RamCache()

    @Provides
    @Singleton
    @InBoxRamCache
    fun provideInBoxCache(): RamCache<BoxPojo> = RamCache()

    @Provides
    @Singleton
    @OutBoxRamCache
    fun provideOutBoxCache(): RamCache<BoxPojo> = RamCache()

    @Provides
    @Singleton
    @InBoxCountRamCache
    fun provideInBoxCountCache(): RamCache<Int> = RamCache()


    @Provides
    @Singleton
    @DiaryRamCache
    fun provideDiaryCache(): RamCache<DiaryPojo> = RamCache()

    @Provides
    @Singleton
    @HomeworkRamCache
    fun provideHomeworkCache(): RamCache<HomeworkPojo> = RamCache()

    @Provides
    @Singleton
    @AdvertBoardRamCache
    fun provideAdvertBoardCache(): RamCache<AdvertBoardPojo> = RamCache()

    @Provides
    @Singleton
    @MeetingRamCache
    fun provideMeetingCache(): RamCache<MeetingPojo> = RamCache()

    @Provides
    @Singleton
    @ClassHourRamCache
    fun provideClassHourCache(): RamCache<ClassHourPojo> = RamCache()

    @Provides
    @Singleton
    @EventsRamCache
    fun provideEventsCache(): RamCache<EventsPojo> = RamCache()
}