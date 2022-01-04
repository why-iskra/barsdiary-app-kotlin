package ru.unit.barsdiary

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.notification.SendingMessageNotification
import ru.unit.barsdiary.notification.SendingMessageResultNotification
import ru.unit.barsdiary.other.InAppTree
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class Application : Application() {

    @Inject
    lateinit var inAppTree: InAppTree

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var sendingMessageNotification: SendingMessageNotification

    @Inject
    lateinit var sendingMessageResultNotification: SendingMessageResultNotification

    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled((!BuildConfig.DEBUG) || settingsDataStore.enableCrashlytics)

        sendingMessageNotification.channel()
        sendingMessageResultNotification.channel()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.plant(inAppTree)
    }
}