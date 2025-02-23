package com.example.mynutrisionpal.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.activities.MainActivity
import com.example.mynutrisionpal.activities.MealActivity
import com.example.mynutrisionpal.databinding.FragmentMealBottomSheetBinding
import com.example.mynutrisionpal.fragments.HomeFragment.HomeFragment
import com.example.mynutrisionpal.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"
class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) }

        observeBottomSheetMeal()

        onBottomSheetDialogClick()
    }
    // Clicking on the bottom sheet will open the Meal activity
    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if(mealName != null && mealThumb != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                    putExtra(HomeFragment.MEAL_NAME, mealName)

                }
                startActivity(intent)
            }
        }
    }
    private var mealName: String? = null
    private var mealThumb: String? = null
    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal->
            Glide.with(this).load(meal.strMealThumb).into(binding.mealImage)
            binding.tvMealNameInBtmsheet.text = meal.strMeal
            binding.tvMealArea.text = meal.strArea
            binding.tvCategory.text = meal.strCategory

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}