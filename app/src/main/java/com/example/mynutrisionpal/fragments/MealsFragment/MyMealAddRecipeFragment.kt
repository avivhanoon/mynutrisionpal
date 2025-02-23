package com.example.mynutrisionpal.fragments.MealsFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.databinding.FragmentMyMealAddRecipeBinding
import com.example.mynutrisionpal.db.MyMeal.MyMeal
import com.example.mynutrisionpal.viewModel.MyMealViewModel
import java.util.Locale

class MyMealAddRecipeFragment : Fragment() {

    private var _binding: FragmentMyMealAddRecipeBinding? = null
    private val binding get() = _binding!!
    private val myMealViewModel: MyMealViewModel by activityViewModels()
    private var imageUri: Uri? = null

    // Speech recognition launcher
    private val speechRecognizer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenText: ArrayList<String>? = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            spokenText?.let { text ->
                // Get current text and append new spoken text
                val currentText = binding.etRecipeInstructions.text.toString()
                val newText = if (currentText.isEmpty()) {
                    text[0]
                } else {
                    "$currentText\n${text[0]}"
                }
                binding.etRecipeInstructions.setText(newText)
            }
        }
    }

    // Image picker launcher
    val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.ivSmallImage.setImageURI(uri)
            requireActivity().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } else {
            // User canceled image selection
            binding.ivSmallImage.setImageResource(R.drawable.nutrition_logo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMealAddRecipeBinding.inflate(inflater, container, false)

        // Set up click listeners
        binding.apply {
            btnAddPhoto.setOnClickListener {
                pickImageLauncher.launch(arrayOf("image/*"))
            }

            btnSubmitMeal.setOnClickListener {
                addMealToDatabase()
            }

            btnRecordRecipe.setOnClickListener {
                startSpeechRecognition()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_recipe_steps))
        }

        try {
            speechRecognizer.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.speech_recognition_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addMealToDatabase() {
        val mealName = binding.etMealName.text.toString()
        val category = binding.etMealCategory.text.toString()
        val area = binding.etMealOrigin.text.toString()
        val ingredients = binding.etMealIngredients.text.toString()
        val steps = binding.etRecipeInstructions.text.toString()

        // Field validation
        when {
            mealName.isEmpty() -> binding.etMealName.error =
                getString(R.string.meal_name_is_required)
            category.isEmpty() -> binding.etMealCategory.error =
                getString(R.string.category_is_required)
            area.isEmpty() -> binding.etMealOrigin.error =
                getString(R.string.origin_is_required)
            ingredients.isEmpty() -> binding.etMealIngredients.error =
                getString(R.string.ingredients_are_required)
            steps.isEmpty() -> binding.etRecipeInstructions.error =
                getString(R.string.steps_are_required)
            else -> {
                val newMeal = MyMeal(
                    mealName = mealName,
                    category = category,
                    area = area,
                    ingredients = ingredients,
                    steps = steps,
                    photo = imageUri?.toString()
                )

                myMealViewModel.addMyMeal(newMeal)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.meal_added_successfully),
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate back and reset form
                findNavController().navigate(R.id.action_myMealAddRecipeFragment_to_mealsFragment)
                clearForm()
            }
        }
    }

    private fun clearForm() {
        binding.apply {
            etMealName.text?.clear()
            etMealCategory.text?.clear()
            etMealOrigin.text?.clear()
            etMealIngredients.text?.clear()
            etRecipeInstructions.text?.clear()
            ivSmallImage.setImageResource(R.drawable.nutrition_logo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}