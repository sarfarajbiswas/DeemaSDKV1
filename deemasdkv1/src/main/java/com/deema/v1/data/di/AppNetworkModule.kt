package com.deema.v1.data.di

import android.content.Context
import com.deema.v1.data.repository.RemoteDataSource
import com.deema.v1.data.repository.RemoteDataSourceImpl
import com.deema.v1.data.retrofit.DeemaApi
import com.deema.v1.data.util.baseUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppNetworkModule {
    val api: DeemaApi
    val remoteDataSource: RemoteDataSource
}

class AppNetworkModuleImpl(
    private val context: Context
): AppNetworkModule {
    override val api: DeemaApi by lazy {

        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(ApiInterceptor(context))
        okHttpClient.build()

        Retrofit.Builder()
            .baseUrl( baseUrl())
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeemaApi::class.java)
    }
    override val remoteDataSource: RemoteDataSource by lazy {
        RemoteDataSourceImpl(api)
    }
}
