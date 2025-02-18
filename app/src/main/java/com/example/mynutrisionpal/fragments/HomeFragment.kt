package com.example.mynutrisionpal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.activities.CategoryMealsActivity
import com.example.mynutrisionpal.activities.MainActivity
import com.example.mynutrisionpal.activities.MealActivity
import com.example.mynutrisionpal.adapters.CategoriesAdapter
import com.example.mynutrisionpal.adapters.mostPopularAdapter
import com.example.mynutrisionpal.databinding.FragmentHomeBinding
import com.example.mynutrisionpal.pojo.MealDetail
import com.example.mynutrisionpal.viewModel.HomeViewModel

class HomeFragment : Fragment(){
    private lateinit var  binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMealDetail: MealDetail
    private lateinit var popularItemsAdapter: mostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.example.mynutrisionpal.fragments.idMeal"
        const val MEAL_NAME = "com.example.mynutrisionpal.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.mynutrisionpal.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.mynutrisionpal.fragments.categoryName"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = mostPopularAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()

        observerRandomMeal()

        onRandomMealClick()

        viewModel.getPopularItems()

        observePopularItemsLiveData()

        onPopularItemClick()

        prepareCategoriesRecyclerView()

        viewModel.getCategories()

        observeCategoriesLiveData()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recviewCategories.apply {
            categoriesAdapter = CategoriesAdapter()
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { Categories ->
                categoriesAdapter.setCategoryList(Categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val  intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            mealList?.let {
                popularItemsAdapter.setMeals(ArrayList(it))  // Avoid casting null
            } ?: Log.e("HomeFragment", "mealList is null!")
        }
    }

    private fun onRandomMealClick(){
        binding.randomMeal.setOnClickListener{
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMealDetail.idMeal)
            intent.putExtra(MEAL_NAME, randomMealDetail.strMeal)
            intent.putExtra(MEAL_THUMB, randomMealDetail.strMealThumb)
            startActivity(intent)
        }
    }
    private fun observerRandomMeal(){
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,  { meal->
                Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMealDetail = meal

        })
    }
}