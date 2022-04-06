package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.ProductSearchRepository

class ProductSearchInteractor(private val productSearchRepository: ProductSearchRepository) {

    suspend fun search(searchExpression: String): List<Product> =
        productSearchRepository.search(searchExpression)

    suspend fun fetchHints(searchExpression: String): List<String> =
        productSearchRepository.fetchHints(searchExpression)
}