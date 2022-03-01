package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.TripDay
import kotlinx.coroutines.flow.Flow

interface TripDayRepository {

    fun fetchTripDay(tripDayId: String): Flow<TripDay?>

    fun fetchTripDays(): Flow<List<TripDay>>

    suspend fun insertTripDay(tripDay: TripDay)
}