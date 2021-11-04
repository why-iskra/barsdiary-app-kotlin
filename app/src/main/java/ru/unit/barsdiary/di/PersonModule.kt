package ru.unit.barsdiary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.unit.barsdiary.data.module.person.PersonRepositoryImpl
import ru.unit.barsdiary.data.module.person.PersonServiceImpl
import ru.unit.barsdiary.domain.person.PersonRepository
import ru.unit.barsdiary.domain.person.PersonService
import ru.unit.barsdiary.domain.person.PersonUseCase
import ru.unit.barsdiary.domain.person.PersonUseCaseImpl
import javax.inject.Singleton

@Module(includes = [PersonModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object PersonModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindPersonRepository(impl: PersonRepositoryImpl): PersonRepository

        @Binds
        abstract fun bindPersonService(impl: PersonServiceImpl): PersonService

        @Binds
        abstract fun bindPersonUseCase(impl: PersonUseCaseImpl): PersonUseCase
    }

}