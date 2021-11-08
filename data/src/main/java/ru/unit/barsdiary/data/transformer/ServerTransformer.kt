package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo
import ru.unit.barsdiary.sdk.response.GetServersResponse
import javax.inject.Inject

class ServerTransformer @Inject constructor() : BaseTransformer<GetServersResponse, List<ServerInfoPojo>> {
    override fun transform(value: GetServersResponse): List<ServerInfoPojo> {
        return value.data?.mapNotNull {
            val name = it.name
            val url = it.url

            if (name.isNullOrBlank() || url.isNullOrBlank()) {
                null
            } else {
                ServerInfoPojo(name, url)
            }
        } ?: emptyList()
    }
}