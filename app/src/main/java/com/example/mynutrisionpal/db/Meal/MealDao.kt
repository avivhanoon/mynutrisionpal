package com.example.mynutrisionpal.db.Meal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mynutrisionpal.pojo.MealDetail

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(mealDetail: MealDetail)

    @Delete
    suspend fun delete(mealDetail: MealDetail)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<MealDetail>>

    @Query("SELECT EXISTS (SELECT 1 FROM mealInformation WHERE idMeal = :id)")
    fun exists(id: String): LiveData<Boolean>
}