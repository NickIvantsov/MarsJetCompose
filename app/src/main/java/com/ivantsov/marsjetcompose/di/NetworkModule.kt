package com.ivantsov.marsjetcompose.di

import com.ivantsov.marsjetcompose.util.net.NetworkObserver
import com.ivantsov.marsjetcompose.util.net.impl.NetworkObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindsNetworkObserver(net: NetworkObserverImpl): NetworkObserver
}