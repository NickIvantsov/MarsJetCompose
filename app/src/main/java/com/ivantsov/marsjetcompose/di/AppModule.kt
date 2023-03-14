package com.ivantsov.marsjetcompose.di

import com.ivantsov.marsjetcompose.data.api.ApiHelper
import com.ivantsov.marsjetcompose.data.api.ApiService
import com.ivantsov.marsjetcompose.data.api.impl.ApiHelperImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("WEB_API")
    fun provideWebAPI(): String = "https://android-kotlin-fun-mars-server.appspot.com/"

    @Provides
    fun provideRetrofit(@Named("WEB_API") webAPI: String, moshi: Moshi): Retrofit {
        return Retrofit.Builder().baseUrl(webAPI)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiHelperModule {
    @Binds
    abstract fun bindApiHelper(apiHelperImpl: ApiHelperImpl): ApiHelper
}