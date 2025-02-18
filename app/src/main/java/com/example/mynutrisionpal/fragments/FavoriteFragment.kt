package com.example.mynutrisionpal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mynutrisionpal.adapters.FavoriteMealAdapter
import com.example.mynutrisionpal.activities.MainActivity
import com.example.mynutrisionpal.viewModel.HomeViewModel
import com.example.mynutrisionpal.databinding.FavMealCardBinding
import com.example.mynutrisionpal.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesMealsAdapter: FavoriteMealAdapter

    override fun onCreate(savedinstanceState: Bundle?) {
        super.onCreate(savedinstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesLiveData().observe(
            requireActivity(),
            favoritesMealsAdapter.differ::submitList
        )
    }

    private fun prepareRecyclerView() {
        favoritesMealsAdapter = FavoriteMealAdapter()
        binding.favRecView.apply {
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            adapter = favoritesMealsAdapter
        }
    }
}