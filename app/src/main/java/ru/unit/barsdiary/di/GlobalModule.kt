package ru.unit.barsdiary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.unit.barsdiary.data.module.global.GlobalRepositoryImpl
import ru.unit.barsdiary.data.module.global.GlobalServiceImpl
import ru.unit.barsdiary.domain.global.GlobalRepository
import ru.unit.barsdiary.domain.global.GlobalService
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.GlobalUseCaseImpl
import javax.inject.Singleton

@Module(includes = [GlobalModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object GlobalModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindGlobalRepository(impl: GlobalRepositoryImpl): GlobalRepository

        @Binds
        abstract fun bindGlobalService(impl: GlobalServiceImpl): GlobalService

        @Binds
        abstract fun bindGlobalUseCase(impl: GlobalUseCaseImpl): GlobalUseCase
    }

}