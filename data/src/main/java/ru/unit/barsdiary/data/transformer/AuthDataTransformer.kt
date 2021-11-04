package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.sdk.BarsEngine
import javax.inject.Inject

class AuthDataTransformer @Inject constructor() : RevertTransformer<BarsEngine.AuthData, AuthDataPojo> {
    override fun transform(value: BarsEngine.AuthData): AuthDataPojo {
        return AuthDataPojo(
            value.serverUrl,
            value.login,
            value.password
        )
    }

    override fun revert(value: AuthDataPojo): BarsEngine.AuthData {
        return BarsEngine.AuthData(
            value.serverUrl,
            value.login,
            value.password
        )
    }
}