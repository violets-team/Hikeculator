package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Product

interface ProductSearchRepository {

    suspend fun search(searchExpression: String): List<Product>

    suspend fun fetchHints(searchExpression: String): List<String>
}