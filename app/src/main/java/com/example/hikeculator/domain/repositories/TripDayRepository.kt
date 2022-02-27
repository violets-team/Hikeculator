package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.TripDay
import kotlinx.coroutines.flow.Flow

interface TripDayRepository {

    suspend fun fetchTripDay(tripDayId: String): TripDay?

    fun fetchTripDays(): Flow<List<TripDay>>

    suspend fun insertTripDay(tripDay: TripDay)
}