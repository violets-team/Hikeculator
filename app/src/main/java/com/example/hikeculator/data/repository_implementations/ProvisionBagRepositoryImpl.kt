package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.*
import com.example.hikeculator.data.fiebase.entities.FirestoreProvisionBag
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.entities.ProvisionBagProduct
import com.example.hikeculator.domain.repositories.ProvisionBagRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProvisionBagRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : ProvisionBagRepository {

    override suspend fun insertProductToProvisionBag(tripId: String, product: ProvisionBagProduct) {
        firestore.getProvisionBagDocument(tripId = tripId)
            .get()
            .await()
            .also { document ->
                document.toObject<FirestoreProvisionBag>()?.apply {
                    val updatedProductList = listOf(
                        * productList.toTypedArray(),
                        product.mapToFirestoreInstance()
                    )

                    document.reference.update(
                        FirestoreProvisionBag::productList.name,
                        updatedProductList
                    )
                }
            }
    }

    override suspend fun updateProduct(tripId: String, updatedProduct: ProvisionBagProduct) {
        firestore.getProvisionBagDocument(tripId = tripId)
            .let { document ->
                document.get()
                    .await()
                    ?.toObject<FirestoreProvisionBag>()
                    ?.let { firestoreProvisionBag ->
                        val updatedFirestoreProvisionBagProduct =
                            updatedProduct.mapToFirestoreInstance()

                        val updatedProductList = firestoreProvisionBag.productList.toMutableList()
                            .apply {
                                val replacementIndex = indexOf(updatedFirestoreProvisionBagProduct)
                                this[replacementIndex] = updatedFirestoreProvisionBagProduct
                            }.toList()

                        document.update(FirestoreProvisionBag::productList.name, updatedProductList)
                    }
            }
    }

    override fun fetchObservableProvisionBag(tripId: String): Flow<ProvisionBag> = callbackFlow {
        val listener = try {
            firestore.getProvisionBagDocument(tripId = tripId)
                .addSnapshotListener { documentSnapshot: DocumentSnapshot?, error ->
                    if (error != null) {
                        close(cause = error)
                    } else {
                        documentSnapshot?.toObject<FirestoreProvisionBag>()
                            ?.mapToProvisionBag()
                            ?.also { provisionBag -> trySend(element = provisionBag) }
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override suspend fun createProvisionBag(
        tripId: String,
        provisionBag: ProvisionBag
    ) {
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