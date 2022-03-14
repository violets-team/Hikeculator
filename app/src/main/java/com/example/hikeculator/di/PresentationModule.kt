package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.presentation.entrance.EntranceViewModel
import com.example.hikeculator.presentation.general_trip_list.GeneralTripViewModel
import com.example.hikeculator.presentation.trip_creating.TripCreatingViewModel
import com.example.hikeculator.presentation.user_profile_creating.UserProfileCreatingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {

    viewModel { EntranceViewModel(firebaseAuthentication = get(), userProfileInteractor = get()) }

    viewModel { UserProfileCreatingViewModel(userProfileInteractor = get()) }

    viewModel { (interactor: TripInteractor) -> GeneralTripViewModel(tripInteractor = interactor) }

    viewModel { (interactor: TripInteractor) ->
        TripCreatingViewModel(
            tripInteractor = interactor,
            memberInteractor = get(),
            tripCreatorUid = get()
        )
    }
}