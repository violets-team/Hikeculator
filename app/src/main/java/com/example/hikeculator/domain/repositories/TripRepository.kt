package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    suspend fun insertTrip(userUid: String, trip: Trip)

    suspend fun removeTrip(userUid: String, tripId: String)

    fun fetchTrips(userUid: String): Flow<Set<Trip>>

    fun fetchTrip(userUid: String, tripId: String): Flow<Trip?>
}