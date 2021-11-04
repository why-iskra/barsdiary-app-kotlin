package ru.unit.barsdiary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.unit.barsdiary.data.module.mark.MarkRepositoryImpl
import ru.unit.barsdiary.data.module.mark.MarkServiceImpl
import ru.unit.barsdiary.domain.mark.MarkRepository
import ru.unit.barsdiary.domain.mark.MarkService
import ru.unit.barsdiary.domain.mark.MarkUseCase
import ru.unit.barsdiary.domain.mark.MarkUseCaseImpl
import javax.inject.Singleton

@Module(includes = [MarkModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object MarkModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindMarkRepository(impl: MarkRepositoryImpl): MarkRepository

        @Binds
        abstract fun bindMarkService(impl: MarkServiceImpl): MarkService

        @Binds
        abstract fun bindMarkUseCase(impl: MarkUseCaseImpl): MarkUseCase
    }

}