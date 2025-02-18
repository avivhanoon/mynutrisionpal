package com.example.mynutrisionpal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynutrisionpal.adapters.CategoryMealsAdapter
import com.example.mynutrisionpal.databinding.ActivityCategoryMealsBinding
import com.example.mynutrisionpal.fragments.HomeFragment
import com.example.mynutrisionpal.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ✅ Initialize ViewModel FIRST before using it
        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        prepareRecyclerView()
        // ✅ Now it's safe to call ViewModel functions
        val categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME) ?: ""
        categoryMealsViewModel.getMealsByCategory(categoryName)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            title = "Number Of Meals: ${mealsList.size.toString()}"
            binding.categoryCount.text = title
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoryMealsAdapter
        }
    }
}
