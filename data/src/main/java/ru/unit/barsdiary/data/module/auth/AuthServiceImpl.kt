package ru.unit.barsdiary.data.module.auth

import ru.unit.barsdiary.data.datastore.AuthDataStore
import ru.unit.barsdiary.data.transformer.AuthDataTransformer
import ru.unit.barsdiary.data.transformer.ChildTransformer
import ru.unit.barsdiary.data.transformer.ServerInfoTransformer
import ru.unit.barsdiary.domain.auth.AuthService
import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val engine: BarsDiaryEngine,
    private val serverInfoTransformer: ServerInfoTransformer,
    private val authDataTransformer: AuthDataTransformer,
    private val childTransformer: ChildTransformer
) : AuthService {
    override suspend fun getServerList(): List<ServerInfoPojo> = BarsDiaryEngine.getServerList().map { serverInfoTransformer.transform(it) }

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