package ru.unit.barsdiary.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import ru.unit.barsdiary.lib.livedata.ObserverLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppNotifications @Inject constructor() {

    val hasInBoxLiveData = MutableLiveData<Boolean>()
    val hasBirthsTodayLiveData = MutableLiveData<Boolean>()

    private val observerLiveData = ObserverLiveData(
        hasInBoxLiveData,
        hasBirthsTodayLiveData
    )

    fun init() {
        hasInBoxLiveData.postValue(false)
        hasBirthsTodayLiveData.postValue(false)
    }

    fun observe(lifecycleOwner: LifecycleOwner, func: (value: Boolean) -> Unit) {
        observerLiveData.observe(lifecycleOwner) { container ->
            val list = container.value.map { it.value }
            func.invoke(!list.all { it == false })
        }
    }

}