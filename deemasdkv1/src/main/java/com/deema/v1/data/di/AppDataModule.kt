package com.deema.v1.data.di

import com.deema.v1.data.repository.RemoteDataSource
import com.deema.v1.data.repository.RemoteDataSourceImpl
import com.deema.v1.data.retrofit.DeemaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDataModule {
    @Singleton
    @Provides
    fun provideHGDataSource(api: DeemaApi): RemoteDataSource {
        return RemoteDataSourceImpl(api = api)
    }
}