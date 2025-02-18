package com.example.mynutrisionpal.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynutrisionpal.db.MealDatabase
import com.example.mynutrisionpal.pojo.Category
import com.example.mynutrisionpal.pojo.CategoryResponse
import com.example.mynutrisionpal.pojo.MealsByAreaList
import com.example.mynutrisionpal.pojo.MealsByArea
import com.example.mynutrisionpal.pojo.MealDetail
import com.example.mynutrisionpal.pojo.MealsList
import com.example.mynutrisionpal.pojo.RandomMealResponse
import com.example.mynutrisionpal.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
): ViewModel() {
    private var randomMealDetailLiveData= MutableLiveData<MealDetail>()
    private var popularItemsLiveDate = MutableLiveData<List<MealsByArea>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealsList> {
            override fun onResponse(call: Call<MealsList>, response: Response<MealsList>) {
                if(response.body() != null){
                    val randomMeal : MealDetail = response.body()!!.meals[0]
                    randomMealDetailLiveData.value = randomMeal
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<MealsList>, t: Throwable) {
                Log.e("HomeViewModel", "API call failed: ${t.message}")
            }
        })
    }
    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Italian").enqueue(object: Callback<MealsByAreaList> {
            override fun onResponse(call: Call<MealsByAreaList>, response: Response<MealsByAreaList>) {
                if (response.body() != null) {
                    popularItemsLiveDate.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByAreaList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun observeRandomMealLiveData(): LiveData<MealDetail>{
        return randomMealDetailLiveData
    }
    fun observePopularItemsLiveData(): LiveData<List<MealsByArea>>{
        return popularItemsLiveDate
    }
    fun observeCategoriesLiveData(): LiveData<List<Category>>{
        return categoriesLiveData
    }
    fun observeFavoritesLiveData(): LiveData<List<MealDetail>>{
        return favoritesMealsLiveData
    }
}


