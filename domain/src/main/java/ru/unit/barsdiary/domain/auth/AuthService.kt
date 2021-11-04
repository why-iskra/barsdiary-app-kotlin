package ru.unit.barsdiary.domain.auth

import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.domain.auth.pojo.PupilPojo
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo

interface AuthService {
    suspend fun getServerList(): List<ServerInfoPojo>
    suspend fun auth(authData: AuthDataPojo)
    fun isParent(): Boolean
    fun getPupils(): List<PupilPojo>
    fun getSelectedPupil(): Int
    suspend fun selectPupil(index: Int)
    suspend fun logout()
}