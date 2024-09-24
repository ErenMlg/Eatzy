package com.softcross.eatzy.di


import android.content.Context
import com.softcross.eatzy.common.ContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InfrastructureModule {

    @Provides
    @Singleton
    fun provideContextProvider(@ApplicationContext context: Context): ContextProvider {
        return ContextProvider(context)
    }
}