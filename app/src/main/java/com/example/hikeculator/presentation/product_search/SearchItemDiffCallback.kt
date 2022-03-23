package com.example.hikeculator.presentation.product_search

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.Product

class SearchItemDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Product, newItem: Product) =
        oldItem == newItem
}