package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.*
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

class TripRepositoryImpl : TripRepository {

    private val firestore = Firebase.firestore

    override suspend fun insertTrip(userUid: String, trip: Trip) {
        trip.mapToFirestoreTrip().apply {
            firestore.getTripDocument(userUid = userUid, tripId = id)
                .set(this)
                .await()
        }
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