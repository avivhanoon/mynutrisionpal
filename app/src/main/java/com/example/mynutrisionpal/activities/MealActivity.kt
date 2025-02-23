package com.example.mynutrisionpal.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.databinding.ActivityMealBinding
import com.example.mynutrisionpal.db.Meal.MealDatabase
import com.example.mynutrisionpal.fragments.HomeFragment.HomeFragment
import com.example.mynutrisionpal.pojo.MealDetail
import com.example.mynutrisionpal.viewModel.MealViewModel
import com.example.mynutrisionpal.viewModel.MealViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink : String
    private lateinit var mealMvvm: MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)

        val viewModelFactory = MealViewModelFactory(mealDatabase)

        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInformationViews()

        loadingCase()

        mealMvvm.getMealDetails((mealId))

        observeMealDetailsLiveData()

        onYoutubeImageClick()

        onFavoriteClick()
    }
    // Set the Click on the save button
    private fun onFavoriteClick() {
        binding.btnSave.setOnClickListener {
            mealToSave?.let { meal ->
                mealMvvm.isMealSavedInDatabase(meal.idMeal).observe(this) { isSaved ->
                    if (isSaved) {
                        Toast.makeText(this, getString(R.string.meal_saved), Toast.LENGTH_SHORT).show()
                    } else {
                        mealMvvm.insertMeal(meal)
                        Toast.makeText(this, getString(R.string.meal_saved), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    // Set the youtube image click and opens a video of the process of the recipe
    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }
    private var mealToSave: MealDetail? = null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this, object : Observer<MealDetail>{
            override fun onChanged(value: MealDetail) {
                onResponseCase()
                val meal = value
                mealToSave = meal
                val ingredients = "${meal.strMeasure1} ${meal.strIngredient1} \n ${meal.strMeasure2} ${meal.strIngredient2} \n ${meal.strMeasure3} ${meal.strIngredient3}" +
                        " \n ${meal.strMeasure4} ${meal.strIngredient4} \n ${meal.strMeasure5} ${meal.strIngredient5} \n ${meal.strMeasure6} ${meal.strIngredient6} \n ${meal.strMeasure7} ${meal.strIngredient7} " +
                        "\n ${meal.strMeasure8} ${meal.strIngredient8} \n ${meal.strMeasure9} ${meal.strIngredient9} \n ${meal.strMeasure10} ${meal.strIngredient10} " +
                        "\n ${meal.strMeasure11} ${meal.strIngredient11} \n ${meal.strMeasure12} ${meal.strIngredient12} \n ${meal.strMeasure13} ${meal.strIngredient13 } \n" +
                        " ${meal.strMeasure14} ${meal.strIngredient14} \n ${meal.strMeasure15} ${meal.strIngredient15} " +
                        "\n ${meal.strMeasure16} ${meal.strIngredient16} \n ${meal.strMeasure17} ${meal.strIngredient17} \n ${meal.strMeasure18} ${meal.strIngredient18} \n" +
                        " ${meal.strMeasure19} ${meal.strIngredient19} \n ${meal.strMeasure20} ${meal.strIngredient20} \n "
                binding.categoryTitle.text = getString(R.string.category, meal.strCategory)
                binding.areaTitle.text = getString(R.string.area, meal.strArea)
                binding.stepsText.text = meal.strInstructions
                binding.ingredientsText.text = ingredients
                youtubeLink = meal.strYoutube!!
                binding.stepsScroll.visibility = View.GONE

                binding.stepsButton.setOnClickListener {
                    binding.stepsButton.setBackgroundColor(getColor(R.color.light_purple))
                    binding.stepsText.setTextColor(getColor(R.color.black))
                    binding.ingredientsText.setTextColor(getColor(R.color.black))
                    binding.stepsScroll.visibility = View.VISIBLE
                    binding.ingredientScroll.visibility = View.GONE
                }
                binding.ingredientsButton.setOnClickListener {
                    binding.ingredientsButton.setBackgroundColor(getColor(R.color.light_purple))
                    binding.stepsText.setTextColor(getColor(R.color.black))
                    binding.ingredientsText.setTextColor(getColor(R.color.black))
                    binding.stepsScroll.visibility = View.GONE
                    binding.ingredientScroll.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setInformationViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.mealImage)
        binding.title.text = mealName
    }

    private fun getMealInformationFromIntent(){
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
    }
    // Loading when there is no connection or low connection

    private fun loadingCase(){
        binding.btnSave.visibility = View.INVISIBLE
        binding.stepsText.visibility = View.INVISIBLE
        binding.stepsButton.visibility = View.INVISIBLE
        binding.ingredientsText.visibility = View.INVISIBLE
        binding.ingredientsButton.visibility = View.INVISIBLE
        binding.areaTitle.visibility = View.INVISIBLE
        binding.categoryTitle.visibility = View.INVISIBLE
        binding.categoryIcon.visibility = View.INVISIBLE
        binding.areaIcon.visibility = View.INVISIBLE
        binding.youtubeTitle.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }


    private fun onResponseCase() {
        binding.btnSave.visibility = View.VISIBLE
        binding.stepsText.visibility = View.VISIBLE
        binding.stepsButton.visibility = View.VISIBLE
        binding.ingredientsText.visibility = View.VISIBLE
        binding.ingredientsButton.visibility = View.VISIBLE
        binding.areaTitle.visibility = View.VISIBLE
        binding.categoryTitle.visibility = View.VISIBLE
        binding.categoryIcon.visibility = View.VISIBLE
        binding.areaIcon.visibility = View.VISIBLE
        binding.youtubeTitle.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }
    }