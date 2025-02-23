package com.example.mynutrisionpal.di

import com.example.mynutrisionpal.db.MyMeal.MyMealDao
import com.example.mynutrisionpal.db.repository.MyMealRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMyMealRepository(myMealDao: MyMealDao): MyMealRepository {
        return MyMealRepository(myMealDao)
    }
}