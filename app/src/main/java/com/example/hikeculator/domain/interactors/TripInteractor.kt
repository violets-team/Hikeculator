package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.repositories.TripRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TripInteractor(
    private val userUid: String,
    private val tripRepository: TripRepository,
) {

    suspend fun insertTrip(trip: Trip) {
        withContext(Dispatchers.IO) { tripRepository.insertTrip(userUid = userUid, trip = trip) }
    }

    suspend fun removeTrip(tripId: String) {
        withContext(Dispatchers.IO) {
            tripRepository.removeTrip(userUid = userUid, tripId = tripId)
        }
    }

    fun fetchTrips(): Flow<Set<Trip>> = tripRepository.fetchTrips(userUid = userUid)

    fun fetchTrip(tripId: String): Flow<Trip?> {
        return tripRepository.fetchTrip(userUid = userUid, tripId = tripId)
    }
}