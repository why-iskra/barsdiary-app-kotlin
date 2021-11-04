package ru.unit.barsdiary.other

import kotlinx.coroutines.sync.Mutex

class SpamGuard {

    private var listIds = mutableListOf<String>()
    private val mutex = Mutex()

    suspend fun guard(id: String, func: suspend (SpamGuardUnlocker) -> Unit) {
        mutex.lock()
        if (!listIds.contains(id)) {
            lock(id)

            mutex.unlock()
            func(SpamGuardUnlocker(id))
        } else {
            mutex.unlock()
        }
    }

    fun unlock(id: String): Boolean {
        return listIds.remove(id)
    }

    private fun lock(id: String) {
        listIds.add(id)
    }

    inner class SpamGuardUnlocker(private val id: String) {
        fun unlock(): Boolean = unlock(id)
    }
}