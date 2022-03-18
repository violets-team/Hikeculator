package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.TripDay
import kotlinx.coroutines.flow.Flow

interface TripDayRepository {

    fun fetchTripDay(tripId: String, tripDayId: String): Flow<TripDay?>

    fun fetchTripDays(tripId: String): Flow<List<TripDay>>

    suspend fun insertTripDay(tripId: String, tripDay: TripDay)
}