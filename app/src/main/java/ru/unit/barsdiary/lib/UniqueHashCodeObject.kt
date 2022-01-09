package ru.unit.barsdiary.lib

import java.util.concurrent.atomic.AtomicInteger

open class UniqueHashCodeObject {

    companion object {
        private var hash = AtomicInteger(1)
    }

    fun getId() = hash.get()

    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return hash.getAndIncrement()
    }
}