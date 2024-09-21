package com.softcross.eatzy.di

import android.content.Context
import androidx.room.Room
import com.softcross.eatzy.data.local.dao.FavoriteFoodsDao
import com.softcross.eatzy.data.local.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LocalModule {

    @Provides
    @ViewModelScoped
    fun provideLocalDataSource(@ApplicationContext context: Context): FavoriteFoodsDao {
        return Room.databaseBuilder(context, Database::class.java, "foods.sqlite")
            .createFromAsset("foods.sqlite").build().getFavoriteFoodsDao()
    }

}