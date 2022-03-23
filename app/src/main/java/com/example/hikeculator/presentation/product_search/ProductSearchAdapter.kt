package com.example.hikeculator.presentation.product_search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemSearchedProductBinding
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.presentation.common.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ProductSearchAdapter : RecyclerView.Adapter<ProductSearchAdapter.FoodSearchViewHolder>() {

    companion object {

        const val WEIGHT_100 = 100
    }

    private val listOfSearchedProducts: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodSearchViewHolder(ItemSearchedProductBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FoodSearchViewHolder, position: Int) {
        val product: Product = listOfSearchedProducts[position]
        val nutrition: NutritionalValue = product.nutritionalValue

        holder.binding.apply {
            textViewFoodName.text = product.name
            textViewCalories.text = getContentCalories(nutrition.calories, root.context)
            textViewFat.text = getContentFat(nutrition.fats, root.context)
            textViewCarbs.text = getContentCarbs(nutrition.carbs, root.context)
            textViewProtein.text = getContentProtein(nutrition.proteins, root.context)
        }
    }

    override fun getItemCount(): Int = listOfSearchedProducts.size

    fun setSearchedList(list: List<Product>) {
        listOfSearchedProducts.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    private fun getContentCalories(calories: Double, context: Context) =
        "${formatValue(calories * WEIGHT_100)} ${context.getString(R.string.text_kcal)}"

    private fun getContentProtein(proteins: Double, context: Context) =
        "${context.getString(R.string.text_protein)} ${formatValue(proteins * WEIGHT_100)}"

    private fun getContentFat(fat: Double, context: Context) =
        "${context.getString(R.string.text_fat)} ${formatValue(fat * WEIGHT_100)}"

    private fun getContentCarbs(carbs: Double, context: Context) =
        "${context.getString(R.string.text_carbs)} ${formatValue(carbs * WEIGHT_100)}"

    private fun formatValue(value: Double): String {
        val formatter = DecimalFormat("0.0#")
        formatter.roundingMode = RoundingMode.HALF_EVEN
        return formatter.format(value)
    }

    class FoodSearchViewHolder(val binding: ItemSearchedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}