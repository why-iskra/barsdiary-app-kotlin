package ru.unit.barsdiary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.unit.barsdiary.data.module.auth.AuthRepositoryImpl
import ru.unit.barsdiary.data.module.auth.AuthServiceImpl
import ru.unit.barsdiary.domain.auth.AuthRepository
import ru.unit.barsdiary.domain.auth.AuthService
import ru.unit.barsdiary.domain.auth.AuthUseCase
import ru.unit.barsdiary.domain.auth.AuthUseCaseImpl
import javax.inject.Singleton

@Module(includes = [AuthModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

        @Binds
        abstract fun bindAuthService(impl: AuthServiceImpl): AuthService

        @Binds
        abstract fun bindAuthUseCase(impl: AuthUseCaseImpl): AuthUseCase
    }

}