package ru.unit.barsdiary.other

import android.util.Log
import timber.log.Timber
import javax.inject.Inject

class InAppTree @Inject constructor(
    private val inAppLog: InAppLog,
) : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ASSERT) {
            inAppLog.wtf(tag, message)
        } else {
            inAppLog.println(priority, tag, message)
        }
    }
}