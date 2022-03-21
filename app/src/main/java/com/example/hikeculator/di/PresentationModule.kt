package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.presentation.entrance.EntranceViewModel
import com.example.hikeculator.presentation.general_trip_list.GeneralTripViewModel
import com.example.hikeculator.presentation.trip_creating.ITripCreatingViewModel
import com.example.hikeculator.presentation.trip_creating.TripCreatingViewModel
import com.example.hikeculator.presentation.trip_details.TripDetailViewModel
import com.example.hikeculator.presentation.user_profile_creating.UserProfileCreatingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { EntranceViewModel(firebaseAuthentication = get(), userProfileInteractor = get()) }

    viewModel { UserProfileCreatingViewModel(userProfileInteractor = get()) }

    viewModel { (tripInteractor: TripInteractor, tripDayInteractor: TripDayInteractor) ->
        GeneralTripViewModel(
            tripInteractor = tripInteractor,
            tripDayInteractor = tripDayInteractor,
            provisionBagInteractor = get()
        )
    }

    viewModel<ITripCreatingViewModel> {
            (
                tripInteractor: TripInteractor,
                tripDayInteractor: TripDayInteractor,
            ),
        ->
        TripCreatingViewModel(
            tripInteractor = tripInteractor,
            memberInteractor = get(),
            tripDayInteractor = tripDayInteractor,
            provisionBagInteractor = get()
        )
    }

    viewModel {
            (
                tripInteractor: TripInteractor,
                tripDayInteractor: TripDayInteractor,
                tripId: String,
            ),
        ->
        TripDetailViewModel(
            tripInteractor = tripInteractor,
            tripDayInteractor = tripDayInteractor,
            tripId = tripId
        )
    }
}