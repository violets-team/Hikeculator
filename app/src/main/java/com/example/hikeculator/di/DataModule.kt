package com.example.hikeculator.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val dataModule = module {

    single<Firebase> { Firebase }

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
}