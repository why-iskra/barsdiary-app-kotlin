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
    val hasMeetingTodayLiveData = MutableLiveData<Boolean>()
    val hasClassHourTodayLiveData = MutableLiveData<Boolean>()
    val hasEventsTodayLiveData = MutableLiveData<Boolean>()

    private val observerLiveData = ObserverLiveData(
        hasInBoxLiveData,
        hasBirthsTodayLiveData,
        hasMeetingTodayLiveData,
        hasClassHourTodayLiveData,
        hasEventsTodayLiveData,
    )

    fun init() {
        hasInBoxLiveData.postValue(false)
        hasBirthsTodayLiveData.postValue(false)
        hasMeetingTodayLiveData.postValue(false)
        hasClassHourTodayLiveData.postValue(false)
        hasEventsTodayLiveData.postValue(false)
    }

    fun observe(lifecycleOwner: LifecycleOwner, func: (value: Boolean) -> Unit) {
        observerLiveData.observe(lifecycleOwner) { container ->
            val list = container.value.map { it.value }
            func.invoke(!list.all { it == false })
        }
    }

}