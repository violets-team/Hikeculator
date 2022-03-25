package com.example.hikeculator.data.fiebase.entities

import com.example.hikeculator.domain.entities.Product

class FirestoreProvisionBag(
    val productList: List<Product> = emptyList()
)