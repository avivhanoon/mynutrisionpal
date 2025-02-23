package com.example.mynutrisionpal.fragments.MealsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.databinding.FragmentMyMealDetailBinding
import com.example.mynutrisionpal.viewModel.MyMealViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyMealDetailFragment : Fragment() {
    private var _binding: FragmentMyMealDetailBinding? = null
    private val myViewModel: MyMealViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMealDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        myViewModel.chosenMyMeal.observe(viewLifecycleOwner) { meal ->
            with(binding) {
                // Set basic meal information
                title.text = meal.mealName
                areaTitle.text = meal.area
                categoryTitle.text = meal.category
                stepsText.text = meal.steps
                ingredientsText.text = meal.ingredients

                // Load meal image
                Glide.with(requireContext())
                    .load(meal.photo)
                    .into(mealImage)

                // Initially hide steps scroll
                stepsScroll.visibility = View.GONE
                ingredientScroll.visibility = View.VISIBLE

                // Setup click listeners
                setupButtonListeners()
            }
        }
    }

    private fun setupButtonListeners() {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.light_purple)
        val inactiveColor = ContextCompat.getColor(requireContext(), R.color.light_purple) // or your default color
        val activeTextColor = ContextCompat.getColor(requireContext(), R.color.black)
        val inactiveTextColor = ContextCompat.getColor(requireContext(), R.color.black) // assuming you have this color

        with(binding) {
            stepsButton.setOnClickListener {
                // Update button colors
                stepsButton.setBackgroundColor(activeColor)
                ingredientsButton.setBackgroundColor(inactiveColor)

                // Update text colors
                stepsText.setTextColor(activeTextColor)
                ingredientsText.setTextColor(inactiveTextColor)

                // Update visibility
                stepsScroll.visibility = View.VISIBLE
                ingredientScroll.visibility = View.GONE
            }

            ingredientsButton.setOnClickListener {
                // Update button colors
                ingredientsButton.setBackgroundColor(activeColor)
                stepsButton.setBackgroundColor(inactiveColor)

                // Update text colors
                ingredientsText.setTextColor(activeTextColor)
                stepsText.setTextColor(inactiveTextColor)

                // Update visibility
                stepsScroll.visibility = View.GONE
                ingredientScroll.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}