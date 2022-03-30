package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.*
import com.example.hikeculator.data.fiebase.entities.FirestoreProvisionBag
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.repositories.ProvisionBagRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProvisionBagRepositoryImpl(
    private val firestore: FirebaseFirestore,
//    private val userUidRepository: UserUidRepositiory,
) : ProvisionBagRepository {

    override suspend fun insertProductToProvisionBag(tripId: String, product: Product) {
        firestore.getProvisionBagCollection(tripId = tripId)
            .get()
            .await()
            ?.documents
            ?.first()
            ?.also { document ->
                document.toObject<FirestoreProvisionBag>()?.apply {
                    val updatedProductList = listOf(
                        *productList.toTypedArray(),
                        product.mapToFirestoreProduct()
                    )

                    document.reference.update(
                        FirestoreProvisionBag::productList.name,
                        updatedProductList
                    )
                }
            }
    }

    override fun fetchProvisionBag(tripId: String): Flow<ProvisionBag> = callbackFlow {
        val listener = try {
            firestore.getProvisionBagCollection(tripId = tripId)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        close(cause = error)
                    } else {
                        querySnapshot?.documents?.first()
                            ?.toObject<FirestoreProvisionBag>()
                            ?.mapToProvisionBag()
                            ?.also { provisionBag -> trySend(element = provisionBag) }
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override suspend fun createProvisionBag(tripId: String, provisionBag: ProvisionBag) {
        firestore.getProvisionBagDocument(tripId = tripId)
            .set(provisionBag.mapToFirestoreProvisionBag())
            .await()
    }

    override suspend fun removeProvisionBag(tripId: String) {
        firestore.getProvisionBagCollection(tripId = tripId)
            .get()
            .await()
            .documents
            .onEach { document -> document.reference.delete() }
    }
}