package ru.unit.barsdiary.data.utils

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class LoggingInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.run {
            val request = request()
            Timber.i("--> ${request.method()} ${request.url()}")

            val response: Response
            val tookMs = measureTimeMillis {
                try {
                    response = proceed(request)
                } catch (e: Exception) {
                    Timber.i("<-- HTTP FAILED: $e")
                    throw e
                }
            }

            Timber.i("<-- ${response.code()}${if (response.message().isEmpty()) "" else ' ' + response.message()} ${
                response.request().url()
            } (${tookMs}ms)")

            response
        }
    }
}