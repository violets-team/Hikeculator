package com.example.hikeculator.presentation.trip_day_details

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.Product

class MealProductItemDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}