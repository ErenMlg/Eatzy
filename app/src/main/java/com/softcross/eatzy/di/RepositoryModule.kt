package com.softcross.eatzy.di

import com.softcross.eatzy.data.repository.CartRepositoryImpl
import com.softcross.eatzy.data.repository.FirebaseRepositoryImpl
import com.softcross.eatzy.data.repository.FoodRepositoryImpl
import com.softcross.eatzy.domain.repository.CartRepository
import com.softcross.eatzy.domain.repository.FirebaseRepository
import com.softcross.eatzy.domain.repository.FoodRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun provideFirebaseRepository(firebaseRepositoryImpl: FirebaseRepositoryImpl): FirebaseRepository

    @Binds
    @ViewModelScoped
    abstract fun provideFoodRepository(foodRepositoryImpl: FoodRepositoryImpl): FoodRepository

    @Binds
    @ViewModelScoped
    abstract fun provideCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

}