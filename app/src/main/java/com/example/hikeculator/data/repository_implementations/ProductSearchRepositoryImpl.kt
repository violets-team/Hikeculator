package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.mapToProductList
import com.example.hikeculator.data.retrofit.ProductSearchAutoCompleteService
import com.example.hikeculator.data.retrofit.ProductSearchService
import com.example.hikeculator.domain.common.isEmptyWithoutSpaces
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.ProductSearchRepository

class ProductSearchRepositoryImpl : ProductSearchRepository {

    override suspend fun search(searchExpression: String): List<Product> {
        return when {
            searchExpression.isEmptyWithoutSpaces() -> listOf()
            else -> ProductSearchService
                .apiService
                .searchFood(searchExpression = searchExpression)
                .mapToProductList()
        }
    }

    override suspend fun fetchHints(searchExpression: String): List<String> {
        return when {
            searchExpression.isEmptyWithoutSpaces() -> listOf()
            else -> ProductSearchAutoCompleteService.apiService
                .fetchHints(searchExpression = searchExpression)
        }
    }
}