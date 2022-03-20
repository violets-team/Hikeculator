package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.presentation.entrance.EntranceViewModel
import com.example.hikeculator.presentation.general_trip_list.GeneralTripViewModel
import com.example.hikeculator.presentation.trip_creating.ITripCreatingViewModel
import com.example.hikeculator.presentation.trip_creating.TripCreatingViewModel
import com.example.hikeculator.presentation.trip_day_details.TripDayDetailViewModel
import com.example.hikeculator.presentation.trip_details.TripDetailViewModel
import com.example.hikeculator.presentation.user_profile_creating.UserProfileCreatingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {

    viewModel { EntranceViewModel(firebaseAuthentication = get(), userProfileInteractor = get()) }

    viewModel { UserProfileCreatingViewModel(userProfileInteractor = get()) }

    viewModel { (interactor: TripInteractor) -> GeneralTripViewModel(tripInteractor = interactor) }

    viewModel<ITripCreatingViewModel> {
            (
                tripInteractor: TripInteractor,
                tripDayInteractor: TripDayInteractor,
                tripCreatorUid: String,
            ),
        ->
        TripCreatingViewModel(
            tripInteractor = tripInteractor,
            memberInteractor = get(),
            tripDayInteractor = tripDayInteractor,
            tripCreatorUid = tripCreatorUid
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