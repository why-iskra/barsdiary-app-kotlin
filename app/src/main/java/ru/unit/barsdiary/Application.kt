package ru.unit.barsdiary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.unit.barsdiary.other.InAppTree
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class Application : Application() {

    @Inject
    lateinit var inAppTree: InAppTree

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.plant(inAppTree)
    }
}