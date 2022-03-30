package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Product

interface SelectedProductRepository {

    suspend fun saveProduct(product: Product)

    suspend fun fetchProduct(): Product
}