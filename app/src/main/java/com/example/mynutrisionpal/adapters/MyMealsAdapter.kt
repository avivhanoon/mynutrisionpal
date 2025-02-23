package com.example.mynutrisionpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.databinding.MyItemRecipeBinding
import com.example.mynutrisionpal.db.MyMeal.MyMeal

class MyMealsAdapter (val myMeals: List<MyMeal>, val callback: MyMealListener): RecyclerView.Adapter<MyMealsAdapter.MyMealViewHolder>() {

    interface MyMealListener {
        fun onItemClicked(index: Int)
        fun onItemLongClicked(index: Int)
    }

    inner class MyMealViewHolder(private val  binding: MyItemRecipeBinding):
    RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        fun bind(myMeal: MyMeal){
            binding.tvMealName.text = myMeal.mealName
            binding.tvMealCategory.text = myMeal.category
            binding.tvMealArea.text = myMeal.area
            Glide.with(binding.root).load(myMeal.photo).into(binding.ivMealImage)
        }
        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }
        override fun onClick(v: View?) {
            callback.onItemClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            return false
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyMealViewHolder(
        MyItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyMealViewHolder, position: Int) =
        holder.bind(myMeals[position])

    override fun getItemCount() = myMeals.size
    fun itemAt(position: Int) = myMeals[position]
}