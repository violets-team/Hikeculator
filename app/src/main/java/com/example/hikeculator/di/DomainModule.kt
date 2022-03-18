package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.example.hikeculator.domain.repositories.TripRepository
import org.koin.dsl.module

val domainModule = module {

    factory { UserProfileInteractor(userProfileRepository = get()) }

    factory { (userUid: String) -> TripInteractor(userUid = userUid, tripRepository = get()) }

    factory { MemberGroupInteractor(memberGroupRepository = get()) }

    factory { (repository: TripDayRepository) -> TripDayInteractor(tripDayRepository = repository) }
}