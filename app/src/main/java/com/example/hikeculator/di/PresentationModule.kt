package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.ProductInteractor
import com.example.hikeculator.presentation.entrance.EntranceViewModel
import com.example.hikeculator.presentation.general_trip_list.GeneralTripViewModel
import com.example.hikeculator.presentation.product_dialogs.add_product.AddOrEditProductDialogViewModel
import com.example.hikeculator.presentation.product_search.ProductSearchViewModel
import com.example.hikeculator.presentation.provision_bag.ProvisionBagViewModel
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
            userUidRepository = get()
        )
    }

    viewModel { UserProfileCreatingViewModel(userProfileInteractor = get()) }

    viewModel {
        GeneralTripViewModel(
            tripInteractor = get(),
            tripDayInteractor = get(),
            provisionBagInteractor = get(),
            userProfileInteractor = get()
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

    viewModel { (tripId: String) ->
        ProductSearchViewModel(
            searchInteractor = get(),
            selectedProductRepository = get(),
            tripId = tripId
        )
    }

    viewModel {
        AddOrEditProductDialogViewModel(
            selectedProductRepository = get(),
            productInteractor = get()
        )
    }

    viewModel { (tripId: String) ->
        ProvisionBagViewModel(tripId = tripId, provisionBagInteractor = get())
    }
}