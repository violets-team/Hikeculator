package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    suspend fun insertTrip(trip: Trip)

    suspend fun removeTrip(tripId: String)

    fun fetchTrips(): Flow<Set<Trip>>

    fun fetchTrip(tripId: String): Flow<Trip?>
}