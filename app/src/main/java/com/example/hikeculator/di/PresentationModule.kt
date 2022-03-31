package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.ProductSearchInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.presentation.entrance.EntranceViewModel
import com.example.hikeculator.presentation.general_trip_list.GeneralTripViewModel
import com.example.hikeculator.presentation.product_search.ProductSearchViewModel
import com.example.hikeculator.presentation.trip_creating.ITripCreatingViewModel
import com.example.hikeculator.presentation.trip_creating.TripCreatingViewModel
import com.example.hikeculator.presentation.trip_details.TripDetailViewModel
import com.example.hikeculator.presentation.user_profile_creating.UserProfileCreatingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        EntranceViewModel(
            firebaseAuthentication = get(),
            userProfileInteractor = get(),
            userUidRepositiory = get()
        )
    }

    viewModel { UserProfileCreatingViewModel(userProfileInteractor = get()) }

    viewModel {
        GeneralTripViewModel(
            tripInteractor = get(),
            tripDayInteractor = get(),
            provisionBagInteractor = get()
        )
    }

    viewModel<ITripCreatingViewModel> {
        TripCreatingViewModel(
            tripInteractor = get(),
            memberInteractor = get(),
            tripDayInteractor = get(),
            provisionBagInteractor = get()
        )
    }

    viewModel { (tripId: String) ->
        TripDetailViewModel(
            tripInteractor = get(),
            tripDayInteractor = get(),
            tripId = tripId,
        )
    }

    viewModel { ProductSearchViewModel(searchInteractor = get(), selectedProductRepository = get()) }

    viewModel { AddOrEditProductDialogViewModel(selectedProductRepository = get()) }

    viewModel { (tripId: String) ->
        ProvisionBagViewModel(tripId = tripId, provisionBagInteractor = get())
    } }