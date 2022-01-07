package ru.unit.barsdiary.productflavor

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [ProductFlavorDevelopingModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object ProductFlavorDevelopingModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindProductFlavorDeveloping(impl: ProductFlavorDevelopingImpl): ProductFlavorDevelopingInterface

    }

}