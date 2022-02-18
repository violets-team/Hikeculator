package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.repositories.TripRepository

class TripInteractor(private val tripRepository: TripRepository) {

    fun insertTrip(trip: Trip) = tripRepository.insertTrip(trip)


    fun removeTrip() = tripRepository.removeTrip()


    fun fetchTrips(): Set<Trip> = tripRepository.fetchTrips()

}