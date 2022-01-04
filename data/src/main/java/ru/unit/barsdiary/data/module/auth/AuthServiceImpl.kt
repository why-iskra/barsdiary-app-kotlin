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

    override suspend fun getServerList(): List<ServerInfoPojo> = serverTransformer.transform(common.getServers())

    override suspend fun auth(authData: AuthDataPojo) {
        engine.setAuthData(authDataTransformer.revert(authData))
        engine.auth()
    }

    override fun prepare(authData: AuthDataPojo) {
        engine.setAuthData(authDataTransformer.revert(authData))
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