package ru.unit.barsdiary.data.module.auth

import ru.unit.barsdiary.data.transformer.AuthDataTransformer
import ru.unit.barsdiary.data.transformer.PupilTransformer
import ru.unit.barsdiary.data.transformer.ServerInfoTransformer
import ru.unit.barsdiary.domain.auth.AuthService
import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.domain.auth.pojo.PupilPojo
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import ru.unit.barsdiary.sdk.BarsEngine
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val barsEngine: BarsEngine,
    private val serverInfoTransformer: ServerInfoTransformer,
    private val authDataTransformer: AuthDataTransformer,
    private val pupilTransformer: PupilTransformer,
) : AuthService {
    override suspend fun getServerList(): List<ServerInfoPojo> = BarsEngine.getServerList().map { serverInfoTransformer.transform(it) }

    override suspend fun auth(authData: AuthDataPojo) {
        barsEngine.auth(authDataTransformer.revert(authData))
    }

    override fun isParent(): Boolean = barsEngine.isParent()

    override fun getPupils(): List<PupilPojo> = barsEngine.pupils.map { pupilTransformer.transform(it) }

    override fun getSelectedPupil(): Int = barsEngine.selectedPupil

    override suspend fun selectPupil(index: Int) {
        barsEngine.changePupil(index)
    }

    override suspend fun logout() {
        barsEngine.logout()
    }
}