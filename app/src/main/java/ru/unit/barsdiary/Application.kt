package ru.unit.barsdiary

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.notification.SendingMessageNotification
import ru.unit.barsdiary.notification.SendingMessageResultNotification
import ru.unit.barsdiary.lib.InAppTree
import ru.unit.barsdiary.productflavor.ProductFlavorDevelopingInterface
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

    @Inject
    lateinit var productFlavorDevelopingInterface: ProductFlavorDevelopingInterface

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(applicationContext)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        sendingMessageNotification.channel()
        sendingMessageResultNotification.channel()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.plant(inAppTree)

        runCatching {
            productFlavorDevelopingInterface.init(applicationContext)
        }
    }
}