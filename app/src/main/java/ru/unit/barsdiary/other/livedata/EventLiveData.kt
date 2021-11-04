package ru.unit.barsdiary.other.livedata

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class EventLiveData : MutableLiveData<EventLiveData.Event>() {

    private val mutex = Mutex()
    private var countLoading = 0

    suspend fun postEventLoading() {
        mutex.withLock {
            if (countLoading == 0) {
                postValue(Event.LOADING)
            }
            countLoading++
        }
    }

    suspend fun postEventLoaded() {
        mutex.withLock {
            if (countLoading > 0) {
                countLoading--
            }

            if (countLoading == 0) {
                postValue(Event.LOADED)
            }
        }
    }

    enum class Event {
        LOADING,
        LOADED
    }
}
