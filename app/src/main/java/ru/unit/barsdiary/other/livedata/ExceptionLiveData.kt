package ru.unit.barsdiary.other.livedata

import androidx.lifecycle.MutableLiveData

class ExceptionLiveData : MutableLiveData<Throwable>() {

    inline fun safety(tryBlock: () -> Unit, catchBlock: () -> Unit) {
        runCatching {
            tryBlock()
        }.onFailure {
            it.printStackTrace()
            postValue(it)
            catchBlock()
        }
    }

    inline fun safety(tryBlock: () -> Unit) {
        safety(tryBlock, {})
    }
}
