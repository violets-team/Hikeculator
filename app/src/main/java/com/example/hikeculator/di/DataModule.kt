package com.example.hikeculator.di

import com.example.hikeculator.data.fiebase.FirebaseAuthentication
import com.example.hikeculator.data.repository_implementations.*
import com.example.hikeculator.domain.repositories.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.math.sign

val dataModule = module {

    single { Firebase }

    single { FirebaseAuth.getInstance() }

    single { Firebase.firestore }

    single { FirebaseAuthentication(firebase = get(), firebaseAuth = get()) }

    single<UserUidRepository> { UserUidRepositoryImpl() }

    single<UserProfileRepository> { UserProfileRepositoryImpl(firestore = get()) }

    single<TripRepository> { TripRepositoryImpl(firestore = get(), userProfileRepository = get()) }

    single<MemberGroupRepository> { MemberGroupRepositoryImpl(firestore = get()) }

    single<TripDayRepository> { TripDayRepositoryImpl(firestore = get()) }

    single<ProvisionBagRepository> { ProvisionBagRepositoryImpl(firestore = get()) }

    single<ProductSearchRepository> { ProductSearchRepositoryImpl() }

    single<SelectedProductRepository> { SelectedProductRepositoryImpl(appContext = androidApplication()) }

    single<ProductRepository> { ProductRepositoryImpl(firestore = get())}
}