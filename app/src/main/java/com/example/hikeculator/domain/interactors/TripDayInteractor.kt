package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.repositories.TripDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TripDayInteractor(private val tripDayRepository: TripDayRepository) {

    fun fetchTripDay(tripId: String, tripDayId: String): Flow<TripDay?> {
        return tripDayRepository.fetchTripDay(tripId = tripId, tripDayId = tripDayId)
    }

    fun fetchTripDays(tripId: String): Flow<List<TripDay>> {
        return tripDayRepository.fetchTripDays(tripId = tripId)
    }

    suspend fun insertTripDay(tripId: String, tripDay: TripDay) {
        withContext(Dispatchers.IO) {
            tripDayRepository.insertTripDay(tripId = tripId, tripDay = tripDay)
        }
    }

    suspend fun removeTripDayCollection(tripId: String) {
        withContext(Dispatchers.Main) { tripDayRepository.removeTripDayCollection(tripId = tripId) }
    }
}