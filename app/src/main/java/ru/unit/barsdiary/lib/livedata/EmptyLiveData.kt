package ru.unit.barsdiary.lib.livedata

import androidx.lifecycle.MutableLiveData
import ru.unit.barsdiary.lib.UniqueHashCodeObject

class EmptyLiveData : MutableLiveData<UniqueHashCodeObject>() {
    fun post() {
        super.postValue(UniqueHashCodeObject())
    }

    fun set() {
        super.setValue(UniqueHashCodeObject())
    }
}
