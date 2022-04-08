package com.example.hikeculator.presentation.product_search

import android.os.Handler
import android.os.Looper
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

class ProductSearchAdapter(
    private val onItemClicked: (Product) -> Unit
) : ListAdapter<Product, ProductSearchAdapter.FoodSearchViewHolder>(SearchItemDiffCallback()) {

    companion object {
        const val DURATION_BETWEEN_SAME_ITEM_CLICK = 500L
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchedProductBinding.inflate(inflater, parent, false)
        return FoodSearchViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: FoodSearchViewHolder, position: Int) {
        holder.bind(product = getItem(position))
    }

    inner class FoodSearchViewHolder(val binding: ItemSearchedProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            setViewContent(product)
            setItemClickListener()
        }

        private fun setViewContent(product: Product) {
            val nutrition: NutritionalValue = product.nutritionalValue.mapToNutritionHundredGrams()
            binding.apply {
                textViewFoodName.text = product.name
                textViewCalories.setViewContent(nutrition.calories, R.string.format_kcal)
                textViewFat.setViewContent(nutrition.fats, R.string.format_fat)
                textViewCarbs.setViewContent(nutrition.carbs, R.string.format_carbs)
                textViewProtein.setViewContent(nutrition.proteins, R.string.format_protein)
            }
        }

        private fun setItemClickListener() {
            binding.root.setOnClickListener { view ->
                view.isClickable  = false

                onItemClicked(getItem(absoluteAdapterPosition))

                Handler(Looper.getMainLooper()).postDelayed({
                    view.isClickable = true
                }, DURATION_BETWEEN_SAME_ITEM_CLICK)
            }
        }

        private fun TextView.setViewContent(value: Double, @StringRes idRes: Int) {
            text = context.getString(idRes, value.roundToTwoDecimalPlaces())
        }
    }
}