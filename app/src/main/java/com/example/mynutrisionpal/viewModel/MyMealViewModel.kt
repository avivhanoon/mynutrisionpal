package com.example.mynutrisionpal.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynutrisionpal.db.MyMeal.MyMeal
import com.example.mynutrisionpal.db.repository.MyMealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyMealViewModel @Inject constructor(
    private val repository: MyMealRepository
) : ViewModel() {  // Change from AndroidViewModel to ViewModel

    val myMeals: LiveData<List<MyMeal>> = repository.getMyMeals()

    private val _chosenMeal = MutableLiveData<MyMeal>()
    val chosenMyMeal: LiveData<MyMeal> get() = _chosenMeal

    fun setMyMeal(myMeal: MyMeal){
        _chosenMeal.value = myMeal
    }

    fun addMyMeal(myMeal: MyMeal){
        viewModelScope.launch {
            repository.addMyMeal(myMeal)
        }
    }

    fun deleteMeal(myMeal: MyMeal){
        viewModelScope.launch {
            repository.deleteMyMeal(myMeal)
        }
    }
}