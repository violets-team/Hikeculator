package com.example.hikeculator.presentation.food_search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.data.retrofit.FoodSearchAPI
import com.example.hikeculator.databinding.FragmentFoodSearchBinding
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.entities.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodSearchFragment : Fragment(R.layout.fragment_food_search) {

    val viewBinding by viewBinding(FragmentFoodSearchBinding::bind)

    private val searchedProductsAdapter = FoodSearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch(Dispatchers.IO) {
            val list = FoodSearchAPI().search("Apple")

            for (product in list) {
                Log.d("TURBO", product.name)
                Log.d("TURBO", product.weight.toString())
                product.nutritionalValue.apply {
                    Log.d("TURBO", "Calories: $calories")
                    Log.d("TURBO", "Carbs: $carbohydrates")
                    Log.d("TURBO", "Proteins: $proteins")
                    Log.d("TURBO", "Fats: $fats")
                    Log.d("TURBO", "-----------------------------------------------------")
                }
            }
        }
        //initializeSearchRecyclerView()
        //setTestDataToRecyclerView()
    }

    private fun setTestDataToRecyclerView() {
        val list = mutableListOf<Product>()

        val product = Product(
            name = "Apple",
            weight = 100.0,
            nutritionalValue = NutritionalValue(123.0, 12.0, 3.0, 46.0)
        )

        for (i in 1..10) {
            list.add(product)
        }

        searchedProductsAdapter.setSearchedList(list)
    }

    private fun initializeSearchRecyclerView() {
        viewBinding.recyclerViewListOfProducts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchedProductsAdapter
        }
    }
}