package ru.unit.barsdiary.sdk

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Common(
    client: OkHttpClient,
) {

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("http://aggregator-obr.bars-open.ru")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(CommonService::class.java)

    suspend fun getServers() = service.getServers()

}