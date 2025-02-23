package com.example.mynutrisionpal.fragments.MealsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.adapters.MyMealsAdapter
import com.example.mynutrisionpal.databinding.FragmentMealsBinding
import com.example.mynutrisionpal.viewModel.MyMealViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealsFragment : Fragment() {
    private var _binding: FragmentMealsBinding? = null
    private val binding get() = _binding!!
    private val myMealViewModel: MyMealViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentMealsBinding.inflate(inflater, container, false)

        binding.btnAddMeal.setOnClickListener {
            findNavController().navigate(R.id.action_mealsFragment_to_myMealAddRecipeFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myMealViewModel.myMeals?.observe(viewLifecycleOwner){
            binding.recyclerViewMeals.adapter = MyMealsAdapter(it, object: MyMealsAdapter.MyMealListener{
                override fun onItemClicked(index: Int) {
                    myMealViewModel.setMyMeal(it[index])
                    findNavController().navigate(R.id.action_mealsFragment_to_myMealDetailFragment)
                }

                override fun onItemLongClicked(index: Int) {
                    return
                }
            })
            binding.recyclerViewMeals.layoutManager = GridLayoutManager(requireContext(), 1)
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val myMeal = (binding.recyclerViewMeals.adapter as MyMealsAdapter).itemAt(viewHolder.adapterPosition)
                myMealViewModel.deleteMeal(myMeal)
            }
        }).attachToRecyclerView(binding.recyclerViewMeals)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}