package com.ashwin.android.currencyconverter.di

import com.ashwin.android.currencyconverter.BuildConfig
import com.ashwin.android.currencyconverter.data.network.CurrencyApi
import com.ashwin.android.currencyconverter.main.DefaultMainRepository
import com.ashwin.android.currencyconverter.main.MainRepository
import com.ashwin.android.currencyconverter.util.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("access_key", BuildConfig.EXCHANGERATESAPI_ACCESS_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatchersProvider = object : DispatchersProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main

        override val io: CoroutineDispatcher
            get() = Dispatchers.IO

        override val default: CoroutineDispatcher
            get() = Dispatchers.Default

        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}
