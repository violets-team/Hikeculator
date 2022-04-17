package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.getTripDayCollection
import com.example.hikeculator.data.common.mapToFirestoreTripDay
import com.example.hikeculator.data.common.mapToTripDay
import com.example.hikeculator.data.fiebase.entities.FirestoreTripDay
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TripDayRepositoryImpl(
    private val firestore: FirebaseFirestore
) : TripDayRepository {

    override fun fetchTripDay(tripId: String, tripDayId: String): Flow<TripDay?> = callbackFlow {
        val listener = try {
            firestore.getTripDayCollection(tripId = tripId)
                .document(tripDayId)
                .addSnapshotListener { document, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    } else {
                        document?.toObject<FirestoreTripDay>()
                            ?.mapToTripDay()
                            ?.also { tripDay -> trySend(element = tripDay) }
                            ?: trySend(element = null)
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override fun fetchTripDays(tripId: String): Flow<List<TripDay>> = callbackFlow {
        val listener = try {
            firestore.getTripDayCollection(tripId = tripId)
                .orderBy(TripDay::date.name)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    } else {
                        querySnapshot?.documents
                            ?.mapNotNull { document -> document?.toObject<FirestoreTripDay>() }
                            ?.map { firestoreTripDay -> firestoreTripDay.mapToTripDay() }
                            ?.also { tripDays -> trySend(element = tripDays) }
                            ?: trySend(emptyList())
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override suspend fun insertTripDay(tripId: String, tripDay: TripDay) {
        val firestoreTripDay = tripDay.mapToFirestoreTripDay()

        firestore.getTripDayCollection(tripId = tripId)
            .document(firestoreTripDay.id)
            .set(firestoreTripDay)
            .await()
    }

    override suspend fun removeTripDayCollection(tripId: String) {
        firestore.getTripDayCollection(tripId = tripId)
            .get()
            .await()
            .documents
            .onEach { document -> document.reference.delete().await() }
    }
}