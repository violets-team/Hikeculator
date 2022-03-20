package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.getTripDocument
import com.example.hikeculator.data.common.getTripSubCollection
import com.example.hikeculator.data.common.mapToFirestoreTrip
import com.example.hikeculator.data.common.mapToTrip
import com.example.hikeculator.data.entities.FirestoreTrip
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.repositories.TripRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TripRepositoryImpl(private val firestore: FirebaseFirestore) : TripRepository {

    override suspend fun insertTrip(userUid: String, trip: Trip) {
        val firestoreTrip = trip.mapToFirestoreTrip()
        firestore.getTripDocument(userUid = userUid, tripId = firestoreTrip.id)
            .set(firestoreTrip)
            .await()
    }

    override suspend fun removeTrip(userUid: String, tripId: String) {
        firestore.getTripDocument(userUid = userUid, tripId = tripId)
            .delete()
            .await()
    }

    override fun fetchTrips(userUid: String): Flow<Set<Trip>> = callbackFlow {
        val listener = try {
            firestore.getTripSubCollection(userUid = userUid)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        close(cause = error)
                    } else {
                        querySnapshot?.run {
                            documents.mapNotNull { document -> document?.toObject<FirestoreTrip>() }
                                .map { firestoreTrip -> firestoreTrip.mapToTrip() }
                                .toSet()
                                .also { trips -> trySend(trips) }
                        }
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override fun fetchTrip(userUid: String, tripId: String): Flow<Trip?> = callbackFlow {
        val listener = try {
            firestore.getTripDocument(userUid = userUid, tripId = tripId)
                .addSnapshotListener { document, error ->
                    if (error != null) {
                        close(cause = null)
                    } else {
                        document?.toObject<FirestoreTrip>()
                            ?.mapToTrip()
                            .also { trip -> trySend(element = trip) }
                    }
                }
        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }
}