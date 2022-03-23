package com.example.hikeculator.presentation.product_search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemSearchedProductBinding
import com.example.hikeculator.domain.common.roundToTwoDecimalPlaces
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.presentation.common.*

class ProductSearchAdapter :
    ListAdapter<Product, ProductSearchAdapter.FoodSearchViewHolder>(SearchItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodSearchViewHolder(ItemSearchedProductBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FoodSearchViewHolder, position: Int) {
        holder.bind(product = getItem(position))
    }

    inner class FoodSearchViewHolder(val binding: ItemSearchedProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            setContent(product)
        }

        private fun setContent(product: Product) {
            val nutrition: NutritionalValue = product.nutritionalValue.mapToNutritionHundredGrams()
            binding.apply {
                textViewFoodName.text = product.name
                textViewCalories.setContent(nutrition.calories, R.string.format_kcal)
                textViewFat.setContent(nutrition.fats, R.string.format_fat)
                textViewCarbs.setContent(nutrition.carbs, R.string.format_carbs)
                textViewProtein.setContent(nutrition.proteins, R.string.format_protein)
            }
        }

        private fun TextView.setContent(value: Double, @StringRes idRes: Int) {
            text = context.getString(idRes, value.roundToTwoDecimalPlaces())
        }
    }
}