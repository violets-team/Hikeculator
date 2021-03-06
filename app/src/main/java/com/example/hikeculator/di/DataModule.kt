package com.example.hikeculator.di

import androidx.work.WorkManager
import com.example.hikeculator.data.fiebase.FirebaseAuthentication
import com.example.hikeculator.data.repository_implementations.*
import com.example.hikeculator.domain.repositories.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single { Firebase }

    single { FirebaseAuth.getInstance() }

    single { Firebase.firestore }

    single { WorkManager.getInstance(androidApplication()) }

    single { FirebaseAuthentication(firebase = get(), firebaseAuth = get()) }

    single<UserUidRepository> { UserUidRepositoryImpl() }

    single<UserProfileRepository> { UserProfileRepositoryImpl(firestore = get()) }

    single<TripRepository> { TripRepositoryImpl(firestore = get(), userProfileRepository = get()) }

    single<MemberGroupRepository> {
        MemberGroupRepositoryImpl(firestore = get(), userProfileRepository = get())
    }

    single<TripDayRepository> { TripDayRepositoryImpl(firestore = get()) }

    single<ProvisionBagRepository> { ProvisionBagRepositoryImpl(firestore = get()) }

    single<ProductSearchRepository> { ProductSearchRepositoryImpl() }

    single<SelectedProductRepository> {
        SelectedProductRepositoryImpl(appContext = androidApplication())
    }

    single<DayMealRepository> { DayMealRepositoryImpl(firestore = get()) }

    single<ProductRepository> {
        ProductRepositoryImpl(firestore = get(), dayMealRepository = get())
    }

}