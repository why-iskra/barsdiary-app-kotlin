package ru.unit.barsdiary.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Base64
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.MessageAttachmentPojo
import ru.unit.barsdiary.notification.NotificationId
import ru.unit.barsdiary.notification.SendingMessageNotification
import ru.unit.barsdiary.notification.SendingMessageResultNotification
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SendingMessageService : Service() {

    companion object {
        fun start(
            context: Context,
            receiverIds: List<String>,
            subject: String,
            message: String,
            filename: String?,
            data: Uri?
        ) {
            context.startService(Intent(context, SendingMessageService::class.java).apply {
                putExtra("receivers", receiverIds.toTypedArray())
                putExtra("subject", subject)
                putExtra("message", message)
                putExtra("filename", filename)
                setData(data)
            })
        }
    }

    @Inject
    lateinit var globalUseCase: GlobalUseCase

    @Inject
    lateinit var notificationId: NotificationId

    @Inject
    lateinit var sendingMessageNotification: SendingMessageNotification

    @Inject
    lateinit var sendingMessageResultNotification: SendingMessageResultNotification

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        CoroutineScope(Dispatchers.IO).launch {
            val notifyId = notificationId.get()
            sendingMessageNotification.create(notifyId)

            runCatching {
                val result = if (intent != null) sendMessage(intent) else false

                globalUseCase.clearOutBox()

                stopSelf(startId)
                sendingMessageResultNotification.create(result)
            }.onFailure {
                Timber.e(it)
                sendingMessageResultNotification.create(false)
                stopSelf(startId)
            }

            sendingMessageNotification.remove(notifyId)
        }

        return START_NOT_STICKY
    }

    private suspend fun sendMessage(intent: Intent): Boolean {
        val receivers = intent.getStringArrayExtra("receivers") ?: return false
        val subject = intent.getStringExtra("subject") ?: return false
        val message = intent.getStringExtra("message") ?: return false
        val filename = intent.getStringExtra("filename")
        val path = intent.data

        if ((filename != null && path == null) || (filename == null && path != null)) {
            return false
        }

        val data = if (path != null) {
            val inputStream = applicationContext.contentResolver.openInputStream(path)
            if (inputStream != null) {
                val bytes = inputStream.readBytes()
                inputStream.close()

                Base64.encodeToString(bytes, Base64.DEFAULT)
            } else {
                throw RuntimeException("Cannot read file")
            }
        } else {
            null
        }

        return globalUseCase.sendMessage(
            receivers.toList(),
            subject,
            message,
            if (filename != null && data != null) MessageAttachmentPojo(filename, data) else null
        )
    }
}
