package com.example.marvelcomicsapp.di

import com.example.marvelcomicsapp.BuildConfig
import com.example.marvelcomicsapp.data.remote.api.ComicApi
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import com.example.marvelcomicsapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("AuthInterceptor")
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val url = it.request()
                .url
                .newBuilder()
                .addQueryParameter("ts", Constants.ts)
                .addQueryParameter("apikey", BuildConfig.PUBLIC_KEY)
                .addQueryParameter("hash", Constants.hashMD5())
                .build()
            val request = it.request().newBuilder().url(url)
            val actualRequest = request.build()
            return@Interceptor it.proceed(actualRequest)
        }
    }

    @Provides
    @Singleton
    @Named("LoggingInterceptor")
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("AuthInterceptor") interceptor: Interceptor,
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor
    ): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)

        return httpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideComicApi(retrofit: Retrofit): ComicApi {
        return retrofit.create(ComicApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarvelComicRepository(
        api: ComicApi
    ): MarvelComicRepository = MarvelComicRepository(api)
}