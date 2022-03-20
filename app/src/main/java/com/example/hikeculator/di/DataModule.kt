package com.example.hikeculator.di

import com.example.hikeculator.data.entities.FirebaseAuthentication
import com.example.hikeculator.data.repository_implementations.MemberGroupRepositoryImpl
import com.example.hikeculator.data.repository_implementations.TripDayRepositoryImpl
import com.example.hikeculator.data.repository_implementations.TripRepositoryImpl
import com.example.hikeculator.data.repository_implementations.UserProfileRepositoryImpl
import com.example.hikeculator.domain.repositories.MemberGroupRepository
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.example.hikeculator.domain.repositories.TripRepository
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val dataModule = module {

    single { Firebase }

    single { FirebaseAuth.getInstance() }

    single { Firebase.firestore }

    single { FirebaseAuthentication(firebase = get(), firebaseAuth = get()) }

    single<UserProfileRepository> {
        UserProfileRepositoryImpl(firestore = get(), firebaseAuth = get())
    }

    single<TripRepository> { TripRepositoryImpl(firestore = get()) }

    single<MemberGroupRepository> { MemberGroupRepositoryImpl(firestore = get()) }

    single<TripDayRepository> { (userUid: String) ->
        TripDayRepositoryImpl(userUid = userUid, firestore = get())
    }
}