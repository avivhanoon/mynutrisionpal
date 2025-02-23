package com.example.mynutrisionpal.db.MyMeal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMyMeal(myMeal: MyMeal)
    @Delete
    suspend fun deleteMyMeal(vararg myMeal: MyMeal)

    @Update
    suspend fun updateMyMeal(myMeal: MyMeal)

    @Query("SELECT * FROM myMeals")
    fun getMyMeals() : LiveData<List<MyMeal>>

    @Query("SELECT * FROM myMeals WHERE id LIKE :id")
    fun getMyMeal(id:Int) : MyMeal

    @Query("DELETE FROM myMeals")
    suspend fun deleteAll()
}