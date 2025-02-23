package com.example.mynutrisionpal.fragments.FavoriteFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.adapters.MealsAdapter
import com.example.mynutrisionpal.activities.MainActivity
import com.example.mynutrisionpal.activities.MealActivity
import com.example.mynutrisionpal.viewModel.HomeViewModel
import com.example.mynutrisionpal.databinding.FragmentFavoriteBinding
import com.example.mynutrisionpal.fragments.HomeFragment.HomeFragment.Companion.MEAL_ID
import com.example.mynutrisionpal.fragments.HomeFragment.HomeFragment.Companion.MEAL_NAME
import com.example.mynutrisionpal.fragments.HomeFragment.HomeFragment.Companion.MEAL_THUMB
import com.example.mynutrisionpal.fragments.bottomsheet.MealBottomSheetFragment
import com.google.android.material.snackbar.Snackbar
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.bumptech.glide.load.engine.GlideException

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesMealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            viewModel = (activity as MainActivity).viewModel
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error initializing ViewModel", e)
            handleError(getString(R.string.error_initializing))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            binding = FragmentFavoriteBinding.inflate(inflater)
            return binding.root
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error inflating view", e)
            handleError(getString(R.string.error_loading_view))
            return null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            prepareRecyclerView()
            observeFavorites()
            setupItemTouchHelper()
            setupClickListeners()
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error in onViewCreated", e)
            handleError(getString(R.string.error_loading_view))
        }
    }
    // set the left or right scroll that will delete the meal
    private fun setupItemTouchHelper() {
        try {
            val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    try {
                        val position = viewHolder.adapterPosition
                        val deletedMeal = favoritesMealsAdapter.differ.currentList[position]
                        viewModel.deleteMeal(deletedMeal)

                        Snackbar.make(requireView(), getString(R.string.meal_deleted), Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                try {
                                    viewModel.insertMeal(deletedMeal)
                                } catch (e: Exception) {
                                    handleError(getString(R.string.error_restoring_meal))
                                }
                            }.show()
                    } catch (e: Exception) {
                        handleError(getString(R.string.error_deleting_meal))
                        // Refresh the adapter to restore the item view
                        favoritesMealsAdapter.notifyDataSetChanged()
                    }
                }
            }

            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favRecView)
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error setting up item touch helper", e)
            handleError(getString(R.string.error_setup))
        }
    }
    // Set Click and Long Click, Click will open Meal Activity, Long Click will open Bottom sheet
    private fun setupClickListeners() {
        try {
            onSavedItemClick()
            onSavedItemLongClick()
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error setting up click listeners", e)
            handleError(getString(R.string.error_setup))
        }
    }

    private fun onSavedItemLongClick() {
        favoritesMealsAdapter.onLongItemClick = { meal ->
            try {
                if (isNetworkAvailable()) {
                    val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
                    mealBottomSheetFragment.show(childFragmentManager, getString(R.string.meal_info))
                } else {
                    handleError(getString(R.string.offline_mode_message))
                }
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error showing bottom sheet", e)
                handleError(getString(R.string.error_showing_details))
            }
        }
    }

    private fun onSavedItemClick() {
        favoritesMealsAdapter.onItemClick = { meal ->
            try {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_NAME, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error opening meal activity", e)
                handleError(getString(R.string.error_opening_meal))
            }
        }
    }

    private fun observeFavorites() {
        try {
            viewModel.observeFavoritesLiveData().observe(viewLifecycleOwner) { meals ->
                try {
                    if (meals.isEmpty()) {
                        showEmptyState()
                    } else {
                        hideEmptyState()
                        favoritesMealsAdapter.differ.submitList(meals)
                    }
                } catch (e: Exception) {
                    Log.e("FavoriteFragment", "Error updating meals list", e)
                    handleError(getString(R.string.error_updating_list))
                }
            }
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error observing favorites", e)
            handleError(getString(R.string.error_loading_favorites))
        }
    }

    private fun prepareRecyclerView() {
        try {
            favoritesMealsAdapter = MealsAdapter()
            binding.favRecView.apply {
                layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                adapter = favoritesMealsAdapter
            }
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error preparing RecyclerView", e)
            handleError(getString(R.string.error_setup))
        }
    }
    // Covering No internet connection case
    private fun isNetworkAvailable(): Boolean {
        try {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val network = connectivityManager?.activeNetwork
            val capabilities = connectivityManager?.getNetworkCapabilities(network)
            return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error checking network", e)
            return false
        }
    }

    private fun showEmptyState() {
        try {
            binding.apply {
                favRecView.visibility = View.GONE
                emptyStateLayout.visibility = View.VISIBLE
                emptyStateText.text = getString(R.string.no_favorites_message)
            }
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error showing empty state", e)
        }
    }

    private fun hideEmptyState() {
        try {
            binding.apply {
                favRecView.visibility = View.VISIBLE
                emptyStateLayout.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error hiding empty state", e)
        }
    }

    private fun handleError(message: String) {
        try {
            view?.let {
                Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("FavoriteFragment", "Error showing error message", e)
        }
    }
}