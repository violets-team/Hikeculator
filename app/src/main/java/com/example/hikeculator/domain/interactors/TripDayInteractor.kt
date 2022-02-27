package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.repositories.TripDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TripDayInteractor(private val tripDayRepository: TripDayRepository) {

    suspend fun fetchTripDay(tripDayId: String): TripDay? = withContext(Dispatchers.IO) {
        tripDayRepository.fetchTripDay(tripDayId = tripDayId)
    }

    fun fetchTripDays(): Flow<List<TripDay>> = tripDayRepository.fetchTripDays()

    suspend fun insertTripDay(tripDay: TripDay) {
        withContext(Dispatchers.IO) { tripDayRepository.insertTripDay(tripDay) }
    }
}