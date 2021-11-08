package ru.unit.barsdiary.sdk

import retrofit2.http.GET
import ru.unit.barsdiary.sdk.response.GetServersResponse

interface CommonService {

    @GET("my_diary")
    suspend fun getServers(): GetServersResponse

}