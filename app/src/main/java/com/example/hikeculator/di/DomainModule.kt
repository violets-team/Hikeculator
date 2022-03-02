package com.example.hikeculator.di

import com.example.hikeculator.data.repository_implementations.UserProfileRepositoryImpl
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import com.example.hikeculator.domain.repositories.UserProfileRepository
import org.koin.dsl.module

val domainModule = module {

    factory<UserProfileRepository> { UserProfileRepositoryImpl() }

    factory<UserProfileInteractor> { UserProfileInteractor(userProfileRepository = get()) }


}