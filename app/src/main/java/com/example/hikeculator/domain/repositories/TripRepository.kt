package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    suspend fun insertTrip(trip: Trip)

    suspend fun removeTrip(trip: Trip)

    suspend fun fetchTrips(vararg tripId: String): Set<Trip>

    fun fetchTrip(tripId: String): Flow<Trip?>

    suspend fun retrieveTrip(tripId: String): Trip?
}