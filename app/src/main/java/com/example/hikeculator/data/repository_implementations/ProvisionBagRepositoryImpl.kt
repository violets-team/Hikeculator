package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.getProvisionBagCollection
import com.example.hikeculator.data.common.getProvisionBagDocument
import com.example.hikeculator.data.common.mapToFirestoreProvisionBag
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.repositories.ProvisionBagRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProvisionBagRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : ProvisionBagRepository {

    override fun insertProductToProvisionBag(product: Product) {
        TODO("Not yet implemented")
    }

    override fun fetchProvisionBag(): ProvisionBag {
        TODO("Not yet implemented")
    }

    override suspend fun createProvisionBag(
        userUid: String,
        tripId: String,
        provisionBag: ProvisionBag,
    ) {
        firestore.getProvisionBagDocument(userUid = userUid, tripId = tripId)
            .set(provisionBag.mapToFirestoreProvisionBag())
            .await()
    }

    override suspend fun removeProvisionBag(userUid: String, tripId: String) {
        firestore.getProvisionBagCollection(userUid = userUid, tripId = tripId)
            .get()
            .await()
            .documents
            .onEach { document -> document.reference.delete() }
    }
}