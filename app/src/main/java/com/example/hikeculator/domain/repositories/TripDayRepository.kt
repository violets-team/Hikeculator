package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.TripDay

interface TripDayRepository {

    fun fetchTripDay(): TripDay

    fun fetchTripDays(): List<TripDay>

    fun insertTripDay(tripDay: TripDay)
}