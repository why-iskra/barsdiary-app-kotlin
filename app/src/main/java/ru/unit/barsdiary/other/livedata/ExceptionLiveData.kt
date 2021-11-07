package ru.unit.barsdiary.other.livedata

import androidx.lifecycle.MutableLiveData
import timber.log.Timber

class ExceptionLiveData : MutableLiveData<Throwable?>() {

    inline fun safety(tryBlock: () -> Unit, catchBlock: () -> Unit) {
        runCatching {
            tryBlock()
        }.onFailure {
            Timber.e(it)
            postValue(it)
            catchBlock()
        }.onSuccess {
            postValue(null)
        }
    }

    inline fun safety(tryBlock: () -> Unit) {
        safety(tryBlock, {})
    }
}
