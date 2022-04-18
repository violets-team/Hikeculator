package com.example.hikeculator.data.repository_implementations

import android.system.Os.remove
import android.util.Log
import com.example.hikeculator.data.common.getTripCollection
import com.example.hikeculator.data.common.getTripDocument
import com.example.hikeculator.data.common.mapToFirestoreTrip
import com.example.hikeculator.data.common.mapToTrip
import com.example.hikeculator.data.fiebase.entities.FirestoreTrip
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.repositories.TripRepository
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TripRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val userProfileRepository: UserProfileRepository,
) : TripRepository {

    override suspend fun insertTrip(trip: Trip) {
        val firestoreTrip = trip.mapToFirestoreTrip()

        firestore.getTripDocument(tripId = firestoreTrip.id)
            .set(firestoreTrip)
            .await()

        trip.memberUids.onEach { memberId ->
            userProfileRepository.addTripIdToUserProfile(userUid = memberId, tripId = trip.id)
        }
    }

    override suspend fun removeTrip(trip: Trip) {
        trip.memberUids.onEach { memberId ->
            userProfileRepository.removeTripIdFromUserProfile(userUid = memberId, tripId = trip.id)
        }

        firestore.getTripDocument(tripId = trip.id)
            .delete()
            .await()
    }

    override suspend fun fetchTrips(vararg tripId: String): Set<Trip> {
        return mutableListOf<Trip>().apply {
            tripId.onEach { id ->

                firestore.getTripDocument(tripId = id)
                    .get()
                    .await()
                    ?.toObject<FirestoreTrip>()
                    ?.mapToTrip()
                    ?.also { trip -> add(trip) }
            }
        }.toSet()
    }

    override fun fetchTrip(tripId: String): Flow<Trip?> = callbackFlow {
        val listener = try {
            firestore.getTripDocument(tripId = tripId)
                .addSnapshotListener { document, error ->
                    if (error != null) {
                        return@addSnapshotListener
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

    override suspend fun retrieveTrip(tripId: String): Trip? {
        return firestore.getTripDocument(tripId = tripId)
            .get()
            .await()
            ?.toObject<FirestoreTrip>()
            ?.mapToTrip()
    }

    override fun fetchObservableTrips(vararg tripIds: String): Flow<Set<Trip>> = callbackFlow {
        val listeners = try {
            val listeners = mutableListOf<ListenerRegistration>()

            for (step in 0..tripIds.size / 10) {
                Log.i("app_log", "fetchObservableTrips: $step")
                val tripIdGroup = tripIds.drop(10 * step).take(10)

                if (tripIdGroup.isNotEmpty()) {
                    firestore.getTripCollection()
                        .whereIn(FirestoreTrip::id.name, tripIdGroup)
                        .addSnapshotListener { querySnapshot, error ->

                            if (error != null) {
                                return@addSnapshotListener
                            } else {
                                querySnapshot?.documents?.mapNotNull { documentSnapshot ->
                                    documentSnapshot.toObject<FirestoreTrip>()?.mapToTrip()
                                }?.also { trips -> trySend(element = trips.toSet()) }
                            }
                        }.also {
                                listenerRegistration -> listeners.add(listenerRegistration)
                            Log.i("app_log", "**** add listeners size: ")
                        }
                }
            }
            Log.i("app_log", "**** add listeners size: ${listeners.size}")
            listeners.toList()

        } catch (e: Exception) {
            null
        }

        awaitClose { listeners?.onEach { it.remove() }}
    }
}