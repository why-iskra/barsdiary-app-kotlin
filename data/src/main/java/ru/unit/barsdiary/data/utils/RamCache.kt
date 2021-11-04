package ru.unit.barsdiary.data.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RamCache<T> {
    private val mutex = Mutex()
    private val cachedData = mutableMapOf<Int, T>()

    suspend fun get(vararg args: Any): T? {
        val hash = args.contentDeepHashCode()
        mutex.withLock {
            return cachedData[hash]
        }
    }

    suspend fun put(value: T, vararg args: Any): T? {
        val hash = args.contentDeepHashCode()
        mutex.withLock {
            return cachedData.putIfAbsent(hash, value)
        }
    }

    suspend fun clear() {
        mutex.withLock {
            cachedData.clear()
        }
    }
}