package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Product

interface ProductSearchRepository {

    fun search(query: String): Set<Product>
}