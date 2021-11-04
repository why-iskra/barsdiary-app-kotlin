package ru.unit.barsdiary.sdk.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import ru.unit.barsdiary.sdk.BarsEngine
import ru.unit.barsdiary.sdk.BuildConfig
import ru.unit.barsdiary.sdk.di.annotation.*
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideChuckerCollector(@ApplicationContext context: Context) = ChuckerCollector(
        context = context,
        showNotification = BuildConfig.DEBUG,
        retentionPeriod = RetentionManager.Period.ONE_DAY
    )

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context, chuckerCollector: ChuckerCollector): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(Long.MAX_VALUE)
            .alwaysReadResponseBody(true)
            .build()
    }

    @Provides
    @Singleton
    @ApiWebClient
    fun provideApiWebClient(chuckerInterceptor: ChuckerInterceptor): HttpClient = HttpClient(OkHttp) {
        expectSuccess = true
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
        install(JsonFeature) {
            serializer = GsonSerializer {
                setPrettyPrinting()
            }
            accept(ContentType.Application.Json)
            accept(ContentType.Text.Html)
        }
        engine {
            if (BuildConfig.DEBUG) {
                addInterceptor(chuckerInterceptor)
            }
        }
    }

    @Provides
    @WebDateFormatter
    fun provideWebDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(BarsEngine.webDateFormatPattern)

    @Provides
    @ChartDateFormatter
    fun provideChartDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(BarsEngine.chartDateFormatPattern)

    @Provides
    @MailDateFormatter
    fun provideMailDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(BarsEngine.mailDateFormatPattern)
}