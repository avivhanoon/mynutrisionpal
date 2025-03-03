package com.example.mynutrisionpal.retrofit

import com.example.mynutrisionpal.pojo.CategoryResponse
import com.example.mynutrisionpal.pojo.MealDetail
import com.example.mynutrisionpal.pojo.MealsByAreaList
import com.example.mynutrisionpal.pojo.MealsByCategoryList
import com.example.mynutrisionpal.pojo.MealsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealsList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealsList>

    @GET("filter.php")
    fun getPopularItems(@Query("a") areaName: String) : Call<MealsByAreaList>

    @GET("filter.php")
    fun getCategoryItems(@Query("c") categoryName: String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryResponse>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery: String): Call<MealsList>
}