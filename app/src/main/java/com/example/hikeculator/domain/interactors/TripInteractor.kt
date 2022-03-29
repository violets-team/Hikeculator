package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.repositories.TripRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TripInteractor(private val tripRepository: TripRepository) {

    suspend fun insertTrip(trip: Trip) {
        withContext(Dispatchers.IO) { tripRepository.insertTrip(trip = trip) }
    }

    suspend fun removeTrip(trip: Trip) {
        withContext(Dispatchers.IO) { tripRepository.removeTrip(trip = trip) }
    }

    suspend fun fetchTrips(): Set<Trip> = withContext(Dispatchers.IO) {
        tripRepository.fetchTrips()
    }

    fun fetchTrip(tripId: String): Flow<Trip?> {
        return tripRepository.fetchTrip(tripId = tripId)
    }
}