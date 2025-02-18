package com.example.mynutrisionpal.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynutrisionpal.db.MealDatabase
import com.example.mynutrisionpal.pojo.MealDetail
import com.example.mynutrisionpal.pojo.MealsList
import com.example.mynutrisionpal.pojo.RandomMealResponse
import com.example.mynutrisionpal.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
): ViewModel() {
    private var mealDetailDetailsLiveData = MutableLiveData<MealDetail>()
    fun getMealDetails(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealsList> {
            override fun onResponse(
                call: Call<MealsList>,
                response: Response<MealsList>) {
                if (response.body() != null) {
                    mealDetailDetailsLiveData.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealsList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }
        })
    }

    fun observeMealDetailLiveData(): LiveData<MealDetail> {
        return mealDetailDetailsLiveData
    }

    fun insertMeal(mealDetail: MealDetail) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(mealDetail)
        }

    }

    fun deleteMeal(mealDetail: MealDetail) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(mealDetail)
        }

    }
}