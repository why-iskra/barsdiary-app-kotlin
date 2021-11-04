package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import ru.unit.barsdiary.sdk.BarsEngine
import javax.inject.Inject

class ServerInfoTransformer @Inject constructor() : BaseTransformer<BarsEngine.ServerInfo, ServerInfoPojo> {
    override fun transform(value: BarsEngine.ServerInfo): ServerInfoPojo {
        return ServerInfoPojo(
            value.name,
            value.url
        )
    }
}