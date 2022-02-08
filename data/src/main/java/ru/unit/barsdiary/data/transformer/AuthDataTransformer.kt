package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.RevertTransformer
import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.sdk.Engine
import javax.inject.Inject

class AuthDataTransformer @Inject constructor() : RevertTransformer<Engine.AuthData, AuthDataPojo> {
    override fun transform(value: Engine.AuthData): AuthDataPojo {
        return AuthDataPojo(
            value.serverUrl,
            value.login,
            value.password
        )
    }

    override fun revert(value: AuthDataPojo): Engine.AuthData {
        return Engine.AuthData(
            value.serverUrl,
            value.login,
            value.password
        )
    }
}