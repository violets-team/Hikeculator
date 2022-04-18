package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.getTripDayDocument
import com.example.hikeculator.data.common.mapToDayMeal
import com.example.hikeculator.data.fiebase.entities.FirestoreDayMeal
import com.example.hikeculator.data.fiebase.entities.FirestoreProduct
import com.example.hikeculator.data.fiebase.entities.FirestoreTripDay
import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.repositories.DayMealRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DayMealRepositoryImpl(private val firestore: FirebaseFirestore) : DayMealRepository {

    override fun fetchDayMeal(
        tripId: String,
        dayId: String,
        mealType: MealType
    ): Flow<DayMeal> = callbackFlow {
        val listener = try {
            firestore.getTripDayDocument(tripId = tripId, tripDayId = dayId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    } else {
                        snapshot?.toObject<FirestoreTripDay>()
                            ?.let { firestoreTripDay ->
                                val firestoreDayMeal = getTypedDayMeal(
                                    firestoreTripDay = firestoreTripDay,
                                    mealType = mealType
                                )

                                trySend(element = firestoreDayMeal.mapToDayMeal())
                            }
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override suspend fun addProductToDayMeal(
        tripId: String,
        firestoreTripDay: FirestoreTripDay,
        firestoreProduct: FirestoreProduct,
        mealType: MealType
    ) {
        val firestoreDayMeal = getTypedDayMeal(
            firestoreTripDay = firestoreTripDay,
            mealType = mealType
        )

        val updatedDayMeal = getUpdatedDayMeal(
            firestoreDayMeal = firestoreDayMeal,
            firestoreProduct = firestoreProduct
        )

        firestore.getTripDayDocument(tripId = tripId, tripDayId = firestoreTripDay.id)
            .updateDayMealByType(updatedDayMeal = updatedDayMeal, mealType = mealType)
    }

    override suspend fun removeProductFromDayMeal(
        tripId: String,
        firestoreTripDay: FirestoreTripDay,
        firestoreProduct: FirestoreProduct,
        mealType: MealType
    ) {
        val firestoreDayMeal = getTypedDayMeal(
            firestoreTripDay = firestoreTripDay,
            mealType = mealType
        )

        val updatedProducts = firestoreDayMeal.products.toMutableList()
            .apply { remove(firestoreProduct) }

        val updatedDayMeal = firestoreDayMeal.copy(products = updatedProducts)

        firestore.getTripDayDocument(tripId = tripId, tripDayId = firestoreTripDay.id)
            .updateDayMealByType(updatedDayMeal = updatedDayMeal, mealType = mealType)
    }

    private suspend fun DocumentReference.updateDayMealByType(
        updatedDayMeal: FirestoreDayMeal,
        mealType: MealType
    ) {
        val dayMealPropertyName = when (mealType) {
            MealType.BREAKFAST -> FirestoreTripDay::breakfast.name
            MealType.LUNCH -> FirestoreTripDay::lunch.name
            MealType.DINNER -> FirestoreTripDay::dinner.name
            MealType.SNACK -> FirestoreTripDay::snack.name
        }

        update(dayMealPropertyName, updatedDayMeal).await()
    }

    private fun getTypedDayMeal(
        firestoreTripDay: FirestoreTripDay,
        mealType: MealType
    ): FirestoreDayMeal {
        return when (mealType) {
            MealType.BREAKFAST -> firestoreTripDay.breakfast
            MealType.LUNCH -> firestoreTripDay.lunch
            MealType.DINNER -> firestoreTripDay.dinner
            MealType.SNACK -> firestoreTripDay.snack
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
}