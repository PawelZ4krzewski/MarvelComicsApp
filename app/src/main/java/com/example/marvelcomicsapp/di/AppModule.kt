package com.example.marvelcomicsapp.di

import com.example.marvelcomicsapp.data.remote.api.ComicApi
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import com.example.marvelcomicsapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val url = it.request()
                .url
                .newBuilder()
                .addQueryParameter("ts",Constants.ts)
                .addQueryParameter("apikey",Constants.PUBLIC_KEY)
                .addQueryParameter("hash",Constants.hashMD5())
                .build()
            val request = it.request().newBuilder().url(url)
            val actualRequest = request.build()
            it.proceed(actualRequest)
        }
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpBuilder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)

        return httpBuilder
            .protocols(mutableListOf(Protocol.HTTP_1_1))
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideComicApi(retrofit: Retrofit): ComicApi{
        return retrofit.create(ComicApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarvelComicRepository(
        api: ComicApi
    ) = MarvelComicRepository(api)
}