package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.ProductSearchRepository

class ProductSearchInteractor(private val productSearchRepository: ProductSearchRepository) {

    fun search(query: String): Set<Product> = productSearchRepository.search(query)

}