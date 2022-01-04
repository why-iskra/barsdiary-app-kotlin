package ru.unit.barsdiary.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.unit.barsdiary.R
import javax.inject.Inject

class SendingMessageResultNotification @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val notificationId: NotificationId
) {
    companion object {
        const val CHANNEL_ID = "sendingMessageResult"
    }

    fun create(successful: Boolean) {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_round_send_24)
            .setContentTitle(applicationContext.getString(R.string.channel_sending_message_result_name))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText(applicationContext.getString(
                if(successful) R.string.success else R.string.error
            ))

        NotificationManagerCompat.from(applicationContext).notify(notificationId.get(), builder.build())
    }

    fun channel() {
        val name = applicationContext.getString(R.string.channel_sending_message_result_name)
        val descriptionText = applicationContext.getString(R.string.channel_sending_message_result_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}