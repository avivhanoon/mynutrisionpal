package com.example.mynutrisionpal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.databinding.MealItemBinding
import com.example.mynutrisionpal.pojo.MealsByCategory
class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {
    private var mealList = ArrayList<MealsByCategory>()

    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    fun setMealsList(mealList: List<MealsByCategory>){
        this.mealList = mealList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }
    inner class CategoryMealsViewModel(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)

        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        val meal = mealList[position]

        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.mealImage)

        holder.binding.mealName.text = meal.strMeal
        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}