package ru.unit.barsdiary.data.di

import android.content.Context
import androidx.annotation.Nullable
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import ru.unit.barsdiary.data.BuildConfig
import ru.unit.barsdiary.data.datastore.AuthDataStore
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.data.di.annotation.*
import ru.unit.barsdiary.data.utils.LoggingInterceptor
import ru.unit.barsdiary.sdk.Common
import ru.unit.barsdiary.sdk.Engine
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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
    @Nullable
    fun provideChuckerCollector(
        @ApplicationContext context: Context,
        settingsDataStore: SettingsDataStore,
    ): ChuckerCollector? {
        return if (settingsDataStore.enableChucker) {
            ChuckerCollector(
                context = context,
                showNotification = BuildConfig.DEBUG,
                retentionPeriod = RetentionManager.Period.ONE_DAY
            )
        } else {
            null
        }
    }

    @Provides
    @Singleton
    @Nullable
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
        chuckerCollector: ChuckerCollector?,
    ): ChuckerInterceptor? {
        return if (chuckerCollector == null) {
            null
        } else {
            ChuckerInterceptor.Builder(context)
                .collector(chuckerCollector)
                .maxContentLength(Long.MAX_VALUE)
                .alwaysReadResponseBody(true)
                .build()
        }
    }

    @Provides
    @Singleton
    @EngineHttpClient
    fun provideEngineHttpClient(
        chuckerInterceptor: ChuckerInterceptor?,
        loggingInterceptor: LoggingInterceptor,
        settingsDataStore: SettingsDataStore,
        @SessionCookieSaver sessionCookieSaver: CookieJar,
        @DataStoreCookieSaver dataStoreCookieSaver: CookieJar,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(settingsDataStore.clientTimeout.toLong(), TimeUnit.SECONDS)
        .readTimeout(settingsDataStore.clientTimeout.toLong(), TimeUnit.SECONDS)
        .writeTimeout(settingsDataStore.clientTimeout.toLong(), TimeUnit.SECONDS)
        .cookieJar(if (settingsDataStore.fastAuth) dataStoreCookieSaver else sessionCookieSaver)
        .apply {
            if (BuildConfig.DEBUG && chuckerInterceptor != null) {
                addInterceptor(chuckerInterceptor)
            }

            addInterceptor(loggingInterceptor)
        }.build()

    @Provides
    @CommonHttpClient
    fun provideCommonHttpClient(
        chuckerInterceptor: ChuckerInterceptor?,
        loggingInterceptor: LoggingInterceptor,
        settingsDataStore: SettingsDataStore,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(settingsDataStore.clientTimeout.toLong(), TimeUnit.SECONDS)
        .callTimeout(settingsDataStore.clientTimeout.toLong(), TimeUnit.SECONDS)
        .readTimeout(settingsDataStore.clientTimeout.toLong(), TimeUnit.SECONDS)
        .writeTimeout(settingsDataStore.clientTimeout.toLong(), TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG && chuckerInterceptor != null) {
                addInterceptor(chuckerInterceptor)
            }

            addInterceptor(loggingInterceptor)
        }.build()

    @Provides
    @Singleton
    fun provideSdkEngine(
        @EngineHttpClient client: OkHttpClient,
    ): Engine = Engine(client)

    @Provides
    fun provideSdkCommon(
        @CommonHttpClient client: OkHttpClient,
    ): Common = Common(client)

    @Provides
    @WebDateFormatter
    fun provideWebDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(Engine.webDateFormatPattern)

    @Provides
    @ChartDateFormatter
    fun provideChartDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(Engine.chartDateFormatPattern)

    @Provides
    @MailDateFormatter
    fun provideMailDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(Engine.mailDateFormatPattern)

    @Provides
    @AdvertBoardDateFormatter
    fun provideAdvertBoardDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(Engine.advertBoardDateFormatPattern)

}