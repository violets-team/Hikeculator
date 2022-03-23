package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.*
import org.koin.dsl.module

val domainModule = module {

    factory { UserProfileInteractor(userProfileRepository = get()) }

    factory { TripInteractor(tripRepository = get()) }

    factory { MemberGroupInteractor(memberGroupRepository = get()) }

    factory { TripDayInteractor(tripDayRepository = get()) }

    factory { ProvisionBagInteractor(provisionBagRepository = get()) }
}