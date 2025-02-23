package com.example.mynutrisionpal.di

import android.content.Context
import com.example.mynutrisionpal.db.Meal.MealDao
import com.example.mynutrisionpal.db.Meal.MealDatabase
import com.example.mynutrisionpal.db.MyMeal.MyMealDao
import com.example.mynutrisionpal.db.MyMeal.MyMealDatabase
import com.example.mynutrisionpal.db.User.UserDao
import com.example.mynutrisionpal.db.User.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMealDatabase(@ApplicationContext context: Context): MealDatabase {
        return MealDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideMealDao(mealDatabase: MealDatabase): MealDao {
        return mealDatabase.mealDao()
    }

    @Provides
    @Singleton
    fun provideMyMealDatabase(@ApplicationContext context: Context): MyMealDatabase {
        return MyMealDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideMyMealDao(myMealDatabase: MyMealDatabase): MyMealDao {
        return myMealDatabase.myMealDao()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }
}