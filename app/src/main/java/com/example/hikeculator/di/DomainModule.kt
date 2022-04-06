package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.*
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.example.hikeculator.domain.repositories.TripRepository
import org.koin.dsl.module

val domainModule = module {

    factory { UserProfileInteractor(userProfileRepository = get(), userUidRepository = get()) }

    factory { TripInteractor(tripRepository = get()) }

    factory { MemberGroupInteractor(memberGroupRepository = get()) }

    factory { TripDayInteractor(tripDayRepository = get()) }

    factory { ProvisionBagInteractor(provisionBagRepository = get()) }

    factory { ProductSearchInteractor(productSearchRepository = get()) }

    factory { ProductInteractor(productRepository = get()) }
}