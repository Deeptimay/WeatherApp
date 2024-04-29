package com.example.weatherapp.di

import com.example.weatherapp.data.network.WeatherAppApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    object HttpClient {
        fun setupOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, apiKeyInterceptor: Interceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(apiKeyInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
        }
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideApiKeyInterceptor(@Named("apiKey") apiKey: String): Interceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url

        val url: HttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("key", apiKey)
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)

        val request: Request = requestBuilder.build()
        chain.proceed(request)
    }

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, apiKeyInterceptor: Interceptor): OkHttpClient {
        return HttpClient.setupOkhttpClient(httpLoggingInterceptor, apiKeyInterceptor)
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named("baseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTrendingApi(retrofit: Retrofit): WeatherAppApi =
        retrofit.create(WeatherAppApi::class.java)

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = "http://api.weatherapi.com/v1/"

    @Provides
    @Named("apiKey")
    fun provideApiKey(): String = "aefd795f848f4f11b30174104242904"
}
