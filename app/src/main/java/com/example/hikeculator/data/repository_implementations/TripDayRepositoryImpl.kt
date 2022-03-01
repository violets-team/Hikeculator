package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.*
import com.example.hikeculator.data.entities.FirestoreTripDay
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TripDayRepositoryImpl(
    private val userTripCollectionId: String,
    private val tripId: String,
) : TripDayRepository {

    private val firestore = Firebase.firestore

    override fun fetchTripDay(tripDayId: String): Flow<TripDay?> = callbackFlow {
        val listener = try {
            firestore.collection(GENERAL_TRIP_COLLECTION_NAME)
                .document(userTripCollectionId)
                .collection(GENERAL_TRIP_SUB_COLLECTION_NAME)
                .document(tripId)
                .collection(TRIP_DAY_COLLECTION_NAME)
                .document(tripDayId)
                .addSnapshotListener { document, error ->
                    if (error != null) {
                        close(cause = error)
                    } else {
                        document?.toObject<FirestoreTripDay>()
                            ?.mapToTripDay()
                            ?.also { tripDay -> offer(element = tripDay) }
                            ?: offer(element = null)
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override fun fetchTripDays(): Flow<List<TripDay>> = callbackFlow {
        val listener = try {
            firestore.collection(GENERAL_TRIP_COLLECTION_NAME)
                .document(userTripCollectionId)
                .collection(GENERAL_TRIP_SUB_COLLECTION_NAME)
                .document(tripId)
                .collection(TRIP_DAY_COLLECTION_NAME).addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        close(cause = error)
                    } else {
                        querySnapshot?.documents
                            ?.mapNotNull { document -> document?.toObject<FirestoreTripDay>() }
                            ?.map { firestoreTripDay -> firestoreTripDay.mapToTripDay() }
                            ?.also { tripDays -> offer(element = tripDays) }
                            ?: offer(emptyList())
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override suspend fun insertTripDay(tripDay: TripDay) {
        val firestoreTripDay = tripDay.mapToFirestoreTripDay()

        firestore.collection(GENERAL_TRIP_COLLECTION_NAME)
            .document(userTripCollectionId)
            .collection(GENERAL_TRIP_SUB_COLLECTION_NAME)
            .document(tripId)
            .collection(TRIP_DAY_COLLECTION_NAME)
            .document(firestoreTripDay.id)
            .set(firestoreTripDay)
            .await()
    }
}