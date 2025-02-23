package com.example.mynutrisionpal.fragments.HomeFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.activities.CategoryMealsActivity
import com.example.mynutrisionpal.activities.MainActivity
import com.example.mynutrisionpal.activities.MealActivity
import com.example.mynutrisionpal.adapters.CategoriesAdapter
import com.example.mynutrisionpal.adapters.mostPopularAdapter
import com.example.mynutrisionpal.databinding.FragmentHomeBinding
import com.example.mynutrisionpal.fragments.bottomsheet.MealBottomSheetFragment
import com.example.mynutrisionpal.pojo.MealDetail
import com.example.mynutrisionpal.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMealDetail: MealDetail
    private lateinit var popularItemsAdapter: mostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
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
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupClickListeners()
        loadData()
    }

    private fun setupRecyclerViews() {
        preparePopularItemsRecyclerView()
        prepareCategoriesRecyclerView()
    }
    // setting all Click listeners on the fragment
    private fun setupClickListeners() {
        onRandomMealClick()
        onPopularItemClick()
        onCategoryClick()
        onPopularItemLongClick()
        onSearchIconClick()
    }

    private fun loadData() {
        try {
            // Load random meal
            observerRandomMeal()

            // Load popular items
            viewModel.getPopularItems()
            observePopularItemsLiveData()

            // Load categories
            viewModel.getCategories()
            observeCategoriesLiveData()
        } catch (e: Exception) {
            handleError(getString(R.string.error_loading_data))
        }
    }

    private fun handleError(message: String) {
        binding.apply {
            // Hide loading indicators if you have any
            progressBar.visibility = View.GONE

            // Show error state
            errorLayout.visibility = View.VISIBLE

            // Show error message
            Snackbar.make(root, message, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    errorLayout.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    loadData()
                }.show()
        }
    }
    // Clicking on search button will open search fragment
    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_searchFragment)
        }
    }
    // long click on popular meal will open the bottom sheet

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, getString(R.string.meal_info))
        }
    }
    // category click will open category adapter
    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recViewCategories.apply {
            categoriesAdapter = CategoriesAdapter()
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            try {
                categories?.let {
                    categoriesAdapter.setCategoryList(it)
                } ?: throw Exception(getString(R.string.error_loading_categories))
            } catch (e: Exception) {
                handleError(e.message ?: getString(R.string.error_loading_categories))
            }
        }
    }
    // popular item click will open the Meal activity
    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            try {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_NAME, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            } catch (e: Exception) {
                handleError(getString(R.string.error_opening_meal))
            }
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
            try {
                mealList?.let {
                    popularItemsAdapter.setMeals(ArrayList(it))
                } ?: throw Exception(getString(R.string.error_loading_popular_items))
            } catch (e: Exception) {
                handleError(e.message ?: getString(R.string.error_loading_popular_items))
                Log.e("HomeFragment", "Error loading popular items", e)
            }
        }
    }
    // click on the big random meal image will open his meal activity
    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            try {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, randomMealDetail.idMeal)
                intent.putExtra(MEAL_NAME, randomMealDetail.strMeal)
                intent.putExtra(MEAL_THUMB, randomMealDetail.strMealThumb)
                startActivity(intent)
            } catch (e: Exception) {
                handleError(getString(R.string.error_opening_random_meal))
            }
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            try {
                meal?.let {
                    Glide.with(this@HomeFragment)
                        .load(it.strMealThumb)
                        .error(R.drawable.loading_gif)
                        .into(binding.imgRandomMeal)

                    this.randomMealDetail = it
                } ?: throw Exception(getString(R.string.error_loading_random_meal))
            } catch (e: Exception) {
                handleError(e.message ?: getString(R.string.error_loading_random_meal))
                Log.e("HomeFragment", "Error loading random meal", e)
            }
        }
    }
}