package androidx.lifecycle

fun <T> LiveData<T>.observeFreshly(owner: LifecycleOwner, observer: Observer<in T>) {
    val sinceVersion = this.version
    this.observe(owner, FreshObserver<T>(observer, this, sinceVersion))
}

fun <T> LiveData<T>.observeForeverFreshly(observer: Observer<in T>, skipPendingValue: Boolean = true) {
    val sinceVersion = this.version
    this.observeForever(FreshObserver<T>(observer, this, sinceVersion))
}

fun <T> LiveData<T>.removeObserverFreshly(observer: Observer<in T>) {
    this.removeObserver(FreshObserver<T>(observer, this, 0))
}

class FreshObserver<T>(
    private val delegate: Observer<in T>,
    private val liveData: LiveData<*>,
    private val sinceVersion: Int,
) : Observer<T> {

    override fun onChanged(t: T) {
        if (liveData.version > sinceVersion) {
            delegate.onChanged(t)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (delegate != (other as FreshObserver<*>).delegate) return false
        return true
    }

    override fun hashCode(): Int {
        return delegate.hashCode()
    }
}