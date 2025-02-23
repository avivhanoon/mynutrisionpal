package com.example.mynutrisionpal.db.MyMeal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myMeals")
data class MyMeal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "mealName") val mealName: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "area") val area: String?,
    @ColumnInfo(name = "ingredients") val ingredients: String?,
    @ColumnInfo(name = "steps") val steps: String?,
    @ColumnInfo(name = "photo") val photo: String? = null
)
