package ru.unit.barsdiary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.unit.barsdiary.data.module.diary.DiaryRepositoryImpl
import ru.unit.barsdiary.data.module.diary.DiaryServiceImpl
import ru.unit.barsdiary.domain.diary.DiaryRepository
import ru.unit.barsdiary.domain.diary.DiaryService
import ru.unit.barsdiary.domain.diary.DiaryUseCase
import ru.unit.barsdiary.domain.diary.DiaryUseCaseImpl
import javax.inject.Singleton

@Module(includes = [DiaryModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object DiaryModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository

        @Binds
        abstract fun bindDiaryService(impl: DiaryServiceImpl): DiaryService

        @Binds
        abstract fun bindDiaryUseCase(impl: DiaryUseCaseImpl): DiaryUseCase
    }

}