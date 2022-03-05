package com.example.hikeculator.presentation.trip_creating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.enums.Seasons
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class TripCreatingViewModel(private val tripInteractor: TripInteractor) : ViewModel() {

    fun createTrip(
        id: String,
        name: String,
        startDate: Long,
        endDate: Long,
        memberCount: Int,
        totalCalories: Double,
        type: TripType,
        difficultyCategory: TripDifficultyCategory,
        season: Seasons,
    ) {
        val trip = Trip(
            id = id,
            name = name,
            startDate = startDate,
            endDate = endDate,
            memberCount = memberCount,
            totalCalories = totalCalories,
            type = type,
            difficultyCategory = difficultyCategory,
            season = season
        )

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(exceptionHandler) { tripInteractor.insertTrip(trip = trip) }
    }
}