package ru.unit.barsdiary.other.livedata

import androidx.lifecycle.MutableLiveData

class EmptyLiveData : MutableLiveData<EmptyLiveData.EmptyData>() {
    fun post() {
        super.postValue(EmptyData())
    }

    fun set() {
        super.setValue(EmptyData())
    }

    class EmptyData {
        companion object {
            private var hash = 0
        }

        fun getId() = hash

        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            hash += 1
            return hash
        }
    }
}
