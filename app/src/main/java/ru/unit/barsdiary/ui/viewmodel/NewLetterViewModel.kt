package ru.unit.barsdiary.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.unit.barsdiary.domain.global.pojo.FoundUserPojo
import ru.unit.barsdiary.other.livedata.EmptyLiveData
import ru.unit.barsdiary.other.livedata.ExceptionLiveData
import javax.inject.Inject

@HiltViewModel
class NewLetterViewModel @Inject constructor(
) : ViewModel() {

    val exceptionLiveData = ExceptionLiveData()

    private val recipients = mutableListOf<FoundUserPojo>()
    val recipientsLiveData = MutableLiveData<List<FoundUserPojo>>(recipients)

    val attachmentLiveData = MutableLiveData<Pair<String, Uri>?>(null)

    val searchTextLiveData = MutableLiveData<String>()

    val searchTriggerLiveData = EmptyLiveData()

    fun file(name: String?, path: Uri?) {
        if (name != null && path != null) {
            attachmentLiveData.postValue(name to path)
        } else {
            attachmentLiveData.postValue(null)
        }
    }

    fun addRecipient(user: FoundUserPojo) {
        if (!recipients.contains(user)) {
            recipients.add(0, user)
        }
        recipientsLiveData.postValue(recipients)
    }

    fun removeRecipient(user: FoundUserPojo) {
        recipients.remove(user)
        recipientsLiveData.postValue(recipients)
    }

    fun getRecipients() = recipients.map { it.profileId.toString() }

    fun prepare() {
        recipients.clear()
        recipientsLiveData.postValue(recipients)
        attachmentLiveData.postValue(null)
        searchTextLiveData.postValue("")
    }

}
