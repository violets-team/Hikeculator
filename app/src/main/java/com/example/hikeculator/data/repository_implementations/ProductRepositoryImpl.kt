package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.getTripDayDocument
import com.example.hikeculator.data.common.mapToFirestoreProduct
import com.example.hikeculator.data.fiebase.entities.FirestoreDayMeal
import com.example.hikeculator.data.fiebase.entities.FirestoreProduct
import com.example.hikeculator.data.fiebase.entities.FirestoreTripDay
import com.example.hikeculator.domain.entities.MealType
import com.example.hikeculator.domain.entities.MealType.*
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.ProductRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(private val firestore: FirebaseFirestore) : ProductRepository {

    override suspend fun insertProduct(
        product: Product,
        tripId: String,
        dayId: String,
        mealType: MealType
    ) {
        firestore.getTripDayDocument(tripId = tripId, tripDayId = dayId)
            .let { documentReference ->
                documentReference.get().await()
                    ?.toObject<FirestoreTripDay>()
                    ?.let { firestoreTripDay ->
                        val firestoreProduct = product.mapToFirestoreProduct()

                        val firestoreDayMeal = getTypedDayMeal(
                            firestoreTripDay = firestoreTripDay,
                            mealType = mealType
                        )

                        val updatedDayMeal = getUpdatedDayMeal(
                            firestoreDayMeal = firestoreDayMeal,
                            firestoreProduct = firestoreProduct
                        )

                        updateDayMeal(
                            documentReference = documentReference,
                            updatedDayMeal = updatedDayMeal,
                            mealType = mealType
                        )
                    }
            }
    }

    private fun getTypedDayMeal(
        firestoreTripDay: FirestoreTripDay,
        mealType: MealType
    ): FirestoreDayMeal {
        return when (mealType) {
            BREAKFAST -> firestoreTripDay.breakfast
            LUNCH -> firestoreTripDay.lunch
            DINNER -> firestoreTripDay.dinner
            SNACK -> firestoreTripDay.snack
        }
    }

    private fun getUpdatedDayMeal(
        firestoreDayMeal: FirestoreDayMeal,
        firestoreProduct: FirestoreProduct
    ): FirestoreDayMeal {
        val productList: List<FirestoreProduct> = firestoreDayMeal.products
        val productIndex = productList.indexOfFirst { it.id == firestoreProduct.id }

        if (productIndex == -1) {
            return addProductAndGetUpdatedDayMeal(
                firestoreDayMeal = firestoreDayMeal,
                firestoreProduct = firestoreProduct
            )
        }

        val updatedFirestoreProductList = productList.toMutableList().apply {
            val updatedFirestoreProduct = this[productIndex].summarizeProduct(firestoreProduct)
            removeAt(productIndex)
            add(updatedFirestoreProduct)
        }

        return FirestoreDayMeal(products = updatedFirestoreProductList)
    }

    private fun addProductAndGetUpdatedDayMeal(
        firestoreDayMeal: FirestoreDayMeal,
        firestoreProduct: FirestoreProduct
    ): FirestoreDayMeal {
        val updatedProductList = firestoreDayMeal.products.toMutableList()
        updatedProductList.add(firestoreProduct)

        return FirestoreDayMeal(products = updatedProductList)
    }

    private fun updateDayMeal(
        documentReference: DocumentReference,
        updatedDayMeal: FirestoreDayMeal,
        mealType: MealType
    ) {
        when (mealType) {
            BREAKFAST -> documentReference.update(FirestoreTripDay::breakfast.name, updatedDayMeal)
            LUNCH -> documentReference.update(FirestoreTripDay::lunch.name, updatedDayMeal)
            DINNER -> documentReference.update(FirestoreTripDay::dinner.name, updatedDayMeal)
            SNACK -> documentReference.update(FirestoreTripDay::snack.name, updatedDayMeal)
        }
    }
}