package ru.unit.barsdiary.data.module.auth

import ru.unit.barsdiary.data.transformer.AuthDataTransformer
import ru.unit.barsdiary.data.transformer.ChildTransformer
import ru.unit.barsdiary.data.transformer.ServerTransformer
import ru.unit.barsdiary.domain.auth.AuthService
import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import ru.unit.barsdiary.sdk.Common
import ru.unit.barsdiary.sdk.Engine
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val engine: Engine,
    private val common: Common,
    private val serverTransformer: ServerTransformer,
    private val authDataTransformer: AuthDataTransformer,
    private val childTransformer: ChildTransformer,
) : AuthService {

    companion object {
        const val REPLACE_FROM_LOGIN = "ForTestGooglePlay"
        const val REPLACE_TO_LOGIN = "АБ25_ИскорцевВР"
    }

    override suspend fun getServerList(): List<ServerInfoPojo> = serverTransformer.transform(common.getServers())

    override suspend fun auth(authData: AuthDataPojo) {
        val replaced = AuthDataPojo(
            authData.serverUrl,
            if(authData.login == REPLACE_FROM_LOGIN) REPLACE_TO_LOGIN else authData.login,
            authData.password
        )

        engine.setAuthData(authDataTransformer.revert(replaced))
        engine.auth()
    }

    override fun prepare(authData: AuthDataPojo) {
        val replaced = AuthDataPojo(
            authData.serverUrl,
            if(authData.login == REPLACE_FROM_LOGIN) REPLACE_TO_LOGIN else authData.login,
            authData.password
        )

        engine.setAuthData(authDataTransformer.revert(replaced))
    }

    override fun isParent(): Boolean = engine.isParent()

    override fun getChildren(): List<ChildPojo> = engine.children().map { childTransformer.transform(it) }

    override fun getSelectedChild(): Int = engine.selectedChild()

    override suspend fun selectChild(index: Int) {
        engine.changeChild(index)
    }

    override suspend fun logout() {
        engine.logout()
    }
}