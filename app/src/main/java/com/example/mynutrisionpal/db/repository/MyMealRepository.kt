package com.example.mynutrisionpal.db.repository

import com.example.mynutrisionpal.db.MyMeal.MyMeal
import com.example.mynutrisionpal.db.MyMeal.MyMealDao
import javax.inject.Inject

class MyMealRepository @Inject constructor(
    private val myMealDao: MyMealDao
) {
    fun getMyMeals() = myMealDao.getMyMeals()

    suspend fun addMyMeal(myMeal: MyMeal) {
        myMealDao.addMyMeal(myMeal)
    }

    suspend fun deleteMyMeal(myMeal: MyMeal) {
        myMealDao.deleteMyMeal(myMeal)
    }

    fun getMyMeal(id: Int): MyMeal = myMealDao.getMyMeal(id)

    suspend fun deleteAll() {
        myMealDao.deleteAll()
    }
}