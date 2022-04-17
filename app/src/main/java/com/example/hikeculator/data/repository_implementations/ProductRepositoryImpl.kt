package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.getTripDayDocument
import com.example.hikeculator.data.common.mapToFirestoreProduct
import com.example.hikeculator.data.fiebase.entities.FirestoreTripDay
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.repositories.DayMealRepository
import com.example.hikeculator.domain.repositories.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val dayMealRepository: DayMealRepository
) : ProductRepository {

    override suspend fun insertProduct(
        product: Product,
        tripId: String,
        dayId: String,
        mealType: MealType
    ) {
        firestore.getTripDayDocument(tripId = tripId, tripDayId = dayId)
            .let { documentReference ->
                documentReference.get()
                    .await()
                    ?.toObject<FirestoreTripDay>()
                    ?.let { firestoreTripDay ->

                        dayMealRepository.addProductToDayMeal(
                            tripId = tripId,
                            firestoreTripDay = firestoreTripDay,
                            firestoreProduct = product.mapToFirestoreProduct(),
                            mealType = mealType
                        )
                    }
            }
    }

    override suspend fun removeProduct(
        product: Product,
        tripId: String,
        dayId: String,
        mealType: MealType
    ) {
        firestore.getTripDayDocument(tripId = tripId, tripDayId = dayId)
            .let { documentReference ->
                documentReference.get()
                    .await()
                    ?.toObject<FirestoreTripDay>()
                    ?.let { firestoreTripDay ->

                        dayMealRepository.removeProductFromDayMeal(
                            tripId = tripId,
                            firestoreTripDay = firestoreTripDay,
                            firestoreProduct = product.mapToFirestoreProduct(),
                            mealType = mealType
                        )
                    }
            }
    }
}