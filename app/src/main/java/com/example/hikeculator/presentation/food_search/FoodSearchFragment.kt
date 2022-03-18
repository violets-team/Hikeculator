package com.example.hikeculator.presentation.food_search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentFoodSearchBinding
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.entities.Product

class FoodSearchFragment : Fragment(R.layout.fragment_food_search) {

    val viewBinding by viewBinding(FragmentFoodSearchBinding::bind)

    private val searchedProductsAdapter = FoodSearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSearchRecyclerView()
        setTestDataToRecyclerView()
    }

    private fun setTestDataToRecyclerView() {
        val list = mutableListOf<Product>()

        val product = Product(
            name = "Apple",
            weight = 100,
            nutritionalValue = NutritionalValue(123, 12, 3, 46)
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