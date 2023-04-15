package com.example.myapplication.data.di

import com.example.myapplication.data.datasource.RemoteDataSource
import com.example.myapplication.data.network.ParseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    fun provideParseApi(retrofit: Retrofit): ParseApi = retrofit.create(ParseApi::class.java)


    @Singleton
    @Provides
    fun provideRemoteDataSource(api: ParseApi) = RemoteDataSource(api)
}