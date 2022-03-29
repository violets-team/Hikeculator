package com.example.hikeculator.data.entities

import com.example.hikeculator.domain.entities.Product

class FirestoreProvisionBag(
    val productList: List<FirestoreProduct> = emptyList()
)