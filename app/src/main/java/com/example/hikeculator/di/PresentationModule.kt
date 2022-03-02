package com.example.hikeculator.di

import com.example.hikeculator.presentation.EntranceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<EntranceViewModel> { EntranceViewModel(firebase = get(), firebaseAuth = get()) }

}