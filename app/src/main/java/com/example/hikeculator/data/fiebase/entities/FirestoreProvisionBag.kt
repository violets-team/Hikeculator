package com.example.hikeculator.data.fiebase.entities

data class FirestoreProvisionBag(
    val productList: List<FirestoreProvisionBagProduct> = emptyList()
)