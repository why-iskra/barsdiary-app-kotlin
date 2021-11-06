package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.auth.pojo.AuthDataPojo
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import javax.inject.Inject

class AuthDataTransformer @Inject constructor() : RevertTransformer<BarsDiaryEngine.AuthData, AuthDataPojo> {
    override fun transform(value: BarsDiaryEngine.AuthData): AuthDataPojo {
        return AuthDataPojo(
            value.serverUrl,
            value.login,
            value.password
        )
    }

    override fun revert(value: AuthDataPojo): BarsDiaryEngine.AuthData {
        return BarsDiaryEngine.AuthData(
            value.serverUrl,
            value.login,
            value.password
        )
    }
}