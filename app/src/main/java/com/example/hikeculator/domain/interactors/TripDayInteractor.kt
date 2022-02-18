package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.repositories.TripDayRepository

class TripDayInteractor(private val tripDayRepository: TripDayRepository) {

    fun fetchTripDay(): TripDay = tripDayRepository.fetchTripDay()


    fun fetchTripDays(): List<TripDay> = tripDayRepository.fetchTripDays()


    fun insertTripDay(tripDay: TripDay) = tripDayRepository.insertTripDay(tripDay)

}