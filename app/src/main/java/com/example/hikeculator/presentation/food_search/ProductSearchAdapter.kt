package com.example.hikeculator.presentation.food_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.databinding.ItemSearchedProductBinding
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.presentation.common.*

class ProductSearchAdapter : RecyclerView.Adapter<ProductSearchAdapter.FoodSearchViewHolder>() {

    private val listOfSearchedProducts: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodSearchViewHolder(ItemSearchedProductBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FoodSearchViewHolder, position: Int) {
        println(position)
        val product: Product = listOfSearchedProducts[position]
        val nutrition: NutritionalValue = product.nutritionalValue

        holder.binding.apply {
            root
            textViewFoodName.text = product.name
            textViewCalories.text = String.format(FORMAT_CALORIES, nutrition.calories * DEFAULT_WEIGHT_100)
            textViewFat.text =  String.format(FORMAT_FAT, nutrition.fats * DEFAULT_WEIGHT_100)
            textViewCarbs.text =  String.format(FORMAT_CARBS, nutrition.carbohydrates * DEFAULT_WEIGHT_100)
            textViewProtein.text = String.format(FORMAT_PROTEIN, nutrition.proteins * DEFAULT_WEIGHT_100)
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

    class FoodSearchViewHolder(val binding: ItemSearchedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}
