package com.example.hikeculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.data.common.mapToTrip
import com.example.hikeculator.data.entities.FirestoreTrip
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripRepository
import com.example.hikeculator.data.repository_implementation.TripRepositoryImpl
import com.example.hikeculator.domain.entities.Trip
import kotlinx.coroutines.flow.*

class GeneralTripViewModel : ViewModel() {

    private val tripRepository: TripRepository = TripRepositoryImpl()
    private val tripInteractor = TripInteractor(tripRepository)

    private val _trips = MutableStateFlow<List<FirestoreTrip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow().map {
        it.map { fr -> fr.mapToTrip() }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun fetchTrips() {
        tripInteractor.fetchTrips()
    }

}