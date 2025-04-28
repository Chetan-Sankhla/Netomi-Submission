package com.example.netomitest.di

import android.content.Context
import com.example.netomitest.data.socket.SocketManager
import com.example.netomitest.utils.NetworkObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePieSocketManager(): SocketManager {
        return SocketManager()
    }

    @Provides
    @Singleton
    fun provideNetworkObserver(@ApplicationContext context: Context): NetworkObserver {
        return NetworkObserver(context)
    }
}