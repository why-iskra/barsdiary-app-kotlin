package ru.unit.barsdiary.notification

import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationId @Inject constructor() {

    private val id = AtomicInteger(0)

    fun get() = id.incrementAndGet()
}