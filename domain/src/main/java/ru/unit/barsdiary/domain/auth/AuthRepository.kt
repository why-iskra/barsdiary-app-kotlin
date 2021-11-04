package ru.unit.barsdiary.domain.auth

import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo

interface AuthRepository {
    fun setAuthData(authData: AuthDataPojo)
    fun getAuthData(): AuthDataPojo?
    fun setSelectedPupil(value: Int)
    fun getSelectedPupil(): Int
    fun clearAuthData()
}