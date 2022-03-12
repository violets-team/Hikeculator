package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.GENERAL_TRIP_COLLECTION_NAME
import com.example.hikeculator.data.common.GENERAL_TRIP_SUB_COLLECTION_NAME
import com.example.hikeculator.data.common.mapToFirestoreTrip
import com.example.hikeculator.data.common.mapToTrip
import com.example.hikeculator.data.entities.FirestoreTrip
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.repositories.TripRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TripRepositoryImpl(private val userUid: String) : TripRepository {

    private val firestore = Firebase.firestore

    override suspend fun insertTrip(trip: Trip) {
        val firestoreTrip = trip.mapToFirestoreTrip()

        firestore.collection(GENERAL_TRIP_COLLECTION_NAME)
            .document(userUid)
            .collection(GENERAL_TRIP_SUB_COLLECTION_NAME)
            .document(firestoreTrip.id)
            .set(firestoreTrip)
            .await()
    }

    override suspend fun removeTrip(tripId: String) {
        firestore.collection(GENERAL_TRIP_COLLECTION_NAME)
            .document(userUid)
            .collection(GENERAL_TRIP_SUB_COLLECTION_NAME)
            .document(tripId)
            .delete()
            .await()
    }

    override fun fetchTrips(): Flow<Set<Trip>> = callbackFlow {
        val listener = try {
            firestore.collection(GENERAL_TRIP_COLLECTION_NAME)
                .document(userUid)
                .collection(GENERAL_TRIP_SUB_COLLECTION_NAME)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        close(cause = error)
                    } else {
                        querySnapshot?.run {
                            documents.mapNotNull { document -> document?.toObject<FirestoreTrip>() }
                                .map { firestoreTrip -> firestoreTrip.mapToTrip() }
                                .toSet()
                                .also { trips -> offer(trips) }
                        }
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }
}