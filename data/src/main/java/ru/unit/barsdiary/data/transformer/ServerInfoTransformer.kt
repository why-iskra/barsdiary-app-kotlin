package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import javax.inject.Inject

class ServerInfoTransformer @Inject constructor() : BaseTransformer<BarsDiaryEngine.ServerInfo, ServerInfoPojo> {
    override fun transform(value: BarsDiaryEngine.ServerInfo): ServerInfoPojo {
        return ServerInfoPojo(
            value.name,
            value.url
        )
    }
}