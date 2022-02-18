package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Trip

interface TripRepository {

    fun insertTrip(trip: Trip)

    fun removeTrip()

    fun fetchTrips(): Set<Trip>
}