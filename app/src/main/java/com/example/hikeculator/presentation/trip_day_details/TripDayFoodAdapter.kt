package com.example.hikeculator.presentation.trip_day_details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemDayMealFoodBinding
import com.example.hikeculator.domain.common.update
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.enums.MealType

class TripDayFoodAdapter(
    private val mealType: MealType,
    private val onClickDeleteItem: (food: Product, mealType: MealType) -> Unit
) : ListAdapter<Product, TripDayFoodAdapter.FoodViewHolder>(MealProductItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_meal_food, parent, false)

        return FoodViewHolder(ItemDayMealFoodBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(food = getItem(holder.absoluteAdapterPosition))
    }

    inner class FoodViewHolder(
        val binding: ItemDayMealFoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Product) {
            with(binding) {
                textViewFoodName.text = food.name

                textViewFoodWeight.text = root.context.getString(R.string.value_gram, food.weight)

                textViewFoodCalories.text = root.context.getString(
                    R.string.format_kcal_long,
                    food.getCalorieAmount().toInt()
                )
                textViewFoodFats.text =
                    root.context.getString(R.string.value_gram_double, food.getFatAmount())

                textViewFoodCarbs.text =
                    root.context.getString(R.string.value_gram_double, food.getCarbsAmount())

                buttonDeleteItem.setOnClickListener {
                    onClickDeleteItem(getItem(absoluteAdapterPosition), mealType)
                }
            }
        }

        private fun <T> getText(@StringRes stringId: Int, vararg values: T): String {
            return binding.root.context.getString(stringId, values)
        }
    }
}