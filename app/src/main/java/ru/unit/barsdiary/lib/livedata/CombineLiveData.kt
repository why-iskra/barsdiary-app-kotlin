package ru.unit.barsdiary.lib.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ru.unit.barsdiary.lib.UniqueHashCodeContainer

class ObserverLiveData<T>(vararg args: LiveData<T>) : MediatorLiveData<UniqueHashCodeContainer<Array<out LiveData<T>>>>() {

    init {
        args.forEach {
            addSource(it) {
                postValue(UniqueHashCodeContainer(args))
            }
        }
    }

}