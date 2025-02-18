package com.example.mynutrisionpal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynutrisionpal.databinding.PopularItemsBinding
import com.example.mynutrisionpal.pojo.MealsByArea

class mostPopularAdapter(): RecyclerView.Adapter<mostPopularAdapter.PopularMealViewHolder>() {
    lateinit var onItemClick:((MealsByArea)-> Unit)
    private var mealsList = ArrayList<MealsByArea>()

    fun setMeals(mealsList: ArrayList<MealsByArea>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.popularImageItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    class PopularMealViewHolder(val binding: PopularItemsBinding):RecyclerView.ViewHolder(binding.root)
}