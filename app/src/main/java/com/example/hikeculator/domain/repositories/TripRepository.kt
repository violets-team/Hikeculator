package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    suspend fun insertTrip(trip: Trip)

    suspend fun removeTrip(trip: Trip)

    suspend fun fetchTrips(vararg tripId: String): Set<Trip>

    fun fetchTrip(tripId: String): Flow<Trip?>
}