package com.example.hikeculator.di

import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import com.example.hikeculator.domain.repositories.TripRepository
import org.koin.dsl.module

val domainModule = module {

    factory { UserProfileInteractor(userProfileRepository = get()) }

    factory { (repository: TripRepository) -> TripInteractor(tripRepository = repository) }

    factory { MemberGroupInteractor(memberGroupRepository = get()) }
}