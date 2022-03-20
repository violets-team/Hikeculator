package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.retrofit.FoodSearchAPI
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.ProductSearchRepository

class ProductSearchRepositoryImpl : ProductSearchRepository {

    override suspend fun search(sarchExpression: String): List<Product> =
        FoodSearchAPI().search(sarchExpression)
}x