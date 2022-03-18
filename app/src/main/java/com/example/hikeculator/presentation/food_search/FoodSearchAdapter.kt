package com.example.hikeculator.presentation.food_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.databinding.ItemSearchedFoodBinding
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.presentation.common.FORMAT_CALORIES
import com.example.hikeculator.presentation.common.FORMAT_CARBS
import com.example.hikeculator.presentation.common.FORMAT_FAT
import com.example.hikeculator.presentation.common.FORMAT_PROTEIN

class FoodSearchAdapter : RecyclerView.Adapter<FoodSearchAdapter.FoodSearchViewHolder>() {

    private val listOfSearchedProducts: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodSearchViewHolder(ItemSearchedFoodBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FoodSearchViewHolder, position: Int) {
        println(position)
        val product: Product = listOfSearchedProducts[position]
        val nutrition: NutritionalValue = product.nutritionalValue

        holder.binding.apply {
            textViewFoodName.text = product.name
            textViewCalories.text = String.format(FORMAT_CALORIES, nutrition.calories.toDouble())
            textViewFat.text =  String.format(FORMAT_FAT, nutrition.fats.toDouble())
            textViewCarbs.text =  String.format(FORMAT_CARBS, nutrition.carbohydrates.toDouble())
            textViewProtein.text = String.format(FORMAT_PROTEIN, nutrition.proteins.toDouble())
        }
    }

    override fun getItemCount(): Int {
        return listOfSearchedProducts.size
    }

    fun setSearchedList(list: List<Product>) {
        listOfSearchedProducts.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    class FoodSearchViewHolder(val binding: ItemSearchedFoodBinding) :
        RecyclerView.ViewHolder(binding.root)
}
