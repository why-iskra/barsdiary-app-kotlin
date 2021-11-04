package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.unit.barsdiary.domain.auth.AuthUseCase
import ru.unit.barsdiary.domain.person.PersonUseCase
import ru.unit.barsdiary.domain.person.pojo.ClassmatePojo
import ru.unit.barsdiary.domain.person.pojo.EmployeePojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksDisciplinePojo
import ru.unit.barsdiary.domain.person.pojo.TotalMarksPojo
import ru.unit.barsdiary.other.livedata.EventLiveData
import ru.unit.barsdiary.other.livedata.ExceptionLiveData
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val personUseCase: PersonUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {
    val exceptionLiveData = ExceptionLiveData()

    val personNameLiveData = MutableLiveData<String?>()
    val personParentNameLiveData = MutableLiveData<String?>()
    val isParentLiveData = MutableLiveData<Boolean>()

    val schoolNameLiveData = MutableLiveData<String?>()
    val schoolAddressLiveData = MutableLiveData<String?>()
    val schoolPhoneLiveData = MutableLiveData<String?>()
    val schoolEmailLiveData = MutableLiveData<String?>()
    val schoolSiteLiveData = MutableLiveData<String?>()

    val employeesLiveData = MutableLiveData<List<EmployeePojo>>()
    val classmatesLiveData = MutableLiveData<List<ClassmatePojo>>()

    val totalMarksLiveData = MutableLiveData<TotalMarksPojo>()

    val eventLiveData = EventLiveData()

    private suspend fun getPersonName() {
        exceptionLiveData.safety {
            val result = personUseCase.getPerson()
            personNameLiveData.postValue(result.selectedPupilName)
        }
    }

    private suspend fun getPersonParentName() {
        exceptionLiveData.safety {
            val result = personUseCase.getPerson()
            personParentNameLiveData.postValue(result.userFullName)
        }
    }

    private fun isParent() {
        exceptionLiveData.safety {
            isParentLiveData.postValue(authUseCase.isParent())
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                authUseCase.logout()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            val asyncPersonName = async { getPersonName() }
            val asyncPersonParentName = async { getPersonParentName() }

            isParent()

            asyncPersonName.await()
            asyncPersonParentName.await()

            eventLiveData.postEventLoaded()
        }
    }

    fun reset() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                personUseCase.clear()
            }
        }
    }

    private suspend fun getSchoolInfo() {
        exceptionLiveData.safety {
            val info = personUseCase.getSchoolInfo()

            schoolNameLiveData.postValue(info.name)
            schoolAddressLiveData.postValue(info.address)
            schoolEmailLiveData.postValue(info.email)
            schoolPhoneLiveData.postValue(info.phone)
            schoolSiteLiveData.postValue(info.siteUrl)

            employeesLiveData.postValue(info.employees)
        }
    }

    private suspend fun getClassInfo() {
        exceptionLiveData.safety {
            val info = personUseCase.getClassInfo()
            classmatesLiveData.postValue(info.classmates)
        }
    }

    fun refreshProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            val asyncSchoolInfo = async { getSchoolInfo() }
            val asyncClassInfo = async { getClassInfo() }

            asyncSchoolInfo.await()
            asyncClassInfo.await()

            eventLiveData.postEventLoaded()
        }
    }

    fun resetProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                personUseCase.clearSchoolInfo()
                personUseCase.clearClassInfo()
            }
        }
    }

    private suspend fun getTotalMarks() {
        exceptionLiveData.safety {
            val result = personUseCase.getTotalMarks()
            totalMarksLiveData.postValue(result)
        }
    }

    fun refreshTotalMarks() {
        viewModelScope.launch(Dispatchers.IO) {
            eventLiveData.postEventLoading()

            getTotalMarks()

            eventLiveData.postEventLoaded()
        }
    }

    fun resetTotalMarks() {
        viewModelScope.launch(Dispatchers.IO) {
            exceptionLiveData.safety {
                personUseCase.clearTotalMarks()
            }
        }
    }

    fun subperiodNames(value: TotalMarksPojo) = value.subperiods.map { v -> v.name ?: "" }.toTypedArray()

    fun filteredDisciplineMarks(value: TotalMarksPojo, code: String) = value.disciplineMarks.map { m ->
        TotalMarksDisciplinePojo(
            m.discipline,
            m.periodMarks.filter { p -> p.subperiodCode == code }
        )
    }
}