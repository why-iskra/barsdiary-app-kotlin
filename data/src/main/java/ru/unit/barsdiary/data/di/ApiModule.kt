package ru.unit.barsdiary.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import ru.unit.barsdiary.data.BuildConfig
import ru.unit.barsdiary.data.datastore.AuthDataStore
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.data.di.annotation.*
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @SessionCookieSaver
    fun provideSessionCookieSaver(
        authDataStore: AuthDataStore,
    ) = object : CookieJar {
        private var savedCookies = mutableListOf<Cookie>()

        override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
            if (url.encodedPath().endsWith("login")) {
                authDataStore.sessionId = cookies.find { it.name() == "sessionid" }?.value()
                savedCookies = cookies
            }
        }

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
            if (!url.encodedPath().endsWith("login")) {
                return savedCookies
            }

            return mutableListOf()
        }
    }

    @Provides
    @Singleton
    @DataStoreCookieSaver
    fun provideDataStoreCookieSaver(
        authDataStore: AuthDataStore,
    ) = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
            if (url.encodedPath().endsWith("login")) {
                authDataStore.sessionId = cookies.find { it.name().lowercase() == "sessionid" }?.value()
            }
        }

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
            val sessionId = authDataStore.sessionId
            if (!url.encodedPath().endsWith("login") && !sessionId.isNullOrEmpty()) {
                return mutableListOf(
                    Cookie.Builder()
                        .name("sessionid")
                        .value(sessionId)
                        .domain(url.host())
                        .build()
                )
            }

            return mutableListOf()
        }
    }

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
    fun provideHttpClient(
        chuckerInterceptor: ChuckerInterceptor,
        settingsDataStore: SettingsDataStore,
        @SessionCookieSaver sessionCookieSaver: CookieJar,
        @DataStoreCookieSaver dataStoreCookieSaver: CookieJar,
    ): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(if (settingsDataStore.fastAuth) dataStoreCookieSaver else sessionCookieSaver)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(chuckerInterceptor)
                addInterceptor { chain ->
                    return@addInterceptor chain.run {
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
        }.build()

    @Provides
    @Singleton
    fun provideSdkEngine(
        client: OkHttpClient,
    ): BarsDiaryEngine = BarsDiaryEngine(client)

    @Provides
    @WebDateFormatter
    fun provideWebDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(BarsDiaryEngine.webDateFormatPattern)

    @Provides
    @ChartDateFormatter
    fun provideChartDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(BarsDiaryEngine.chartDateFormatPattern)

    @Provides
    @MailDateFormatter
    fun provideMailDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(BarsDiaryEngine.mailDateFormatPattern)

}