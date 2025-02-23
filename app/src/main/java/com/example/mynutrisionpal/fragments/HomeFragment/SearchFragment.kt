package com.example.mynutrisionpal.fragments.HomeFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynutrisionpal.activities.MainActivity
import com.example.mynutrisionpal.activities.MealActivity
import com.example.mynutrisionpal.adapters.MealsAdapter
import com.example.mynutrisionpal.databinding.FragmentSearchBinding
import com.example.mynutrisionpal.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        onMealItemClick()
        observeSearchMealLiveData()
        var serchJob: Job? = null

        // waiting 5 seconds to show all the meals that have a match to the search
        binding.edSearch.addTextChangedListener { searchQuery ->
            serchJob?.cancel()
            serchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal((searchQuery.toString()))
            }
        }
    }
    private fun observeSearchMealLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer { mealsList->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearch.text.toString()
        if (searchQuery.isNotEmpty()){
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.recyclerViewSearchMeals.apply {
            layoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }
    private fun onMealItemClick() {
        searchRecyclerViewAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java).apply {
                putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }

}