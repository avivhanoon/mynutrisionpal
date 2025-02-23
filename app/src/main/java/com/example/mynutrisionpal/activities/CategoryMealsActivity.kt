package com.example.mynutrisionpal.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.adapters.CategoryMealsAdapter
import com.example.mynutrisionpal.databinding.ActivityCategoryMealsBinding
import com.example.mynutrisionpal.fragments.HomeFragment.HomeFragment
import com.example.mynutrisionpal.viewModel.CategoryMealsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    companion object {
        const val MEAL_ID = "com.example.mynutrisionpal.fragments.idMeal"
        const val MEAL_NAME = "com.example.mynutrisionpal.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.mynutrisionpal.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.mynutrisionpal.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        prepareRecyclerView()
        val categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME) ?: ""
        categoryMealsViewModel.getMealsByCategory(categoryName)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            title = getString(R.string.number_of_meals, mealsList.size.toString())
            binding.categoryCount.text = title
            categoryMealsAdapter.setMealsList(mealsList)
        })
        onMealClick()
    }
    //Click on meal inside the adapter
    private fun onMealClick() {
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }
    // preparing the recycler view in the adapter
    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoryMealsAdapter
        }
    }
}
