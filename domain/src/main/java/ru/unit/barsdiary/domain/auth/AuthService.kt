package ru.unit.barsdiary.domain.auth

import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo

interface AuthService {
    suspend fun getServerList(): List<ServerInfoPojo>
    suspend fun auth(authData: AuthDataPojo)

    fun prepare(authData: AuthDataPojo)

    fun isParent(): Boolean
    fun getChildren(): List<ChildPojo>
    fun getSelectedChild(): Int
    suspend fun selectChild(index: Int)
    suspend fun logout()
}