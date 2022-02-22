package com.example.hikeculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.data.repository_implementations.UserProfileRepositoryImpl
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import com.example.hikeculator.domain.repositories.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileCreatingViewModel : ViewModel() {

    private val userProfileRepository: UserProfileRepository = UserProfileRepositoryImpl()
    private val userProfileInteractor = UserProfileInteractor(userProfileRepository)

    fun createUser(
        name: String,
        email: String,
        password: String,
        token: String,
        age: Int,
        weight: Double,
        height: Double,
        gender: String,
    ) {
        val calorieNorm = NutritionalCalculator().calculateCalorieNorm()

        val user = User(
            name = name,
            email = email,
            password = password,
            token = token,
            age = age,
            weight = weight,
            height = height,
            gender = gender,
            calorieNorm = calorieNorm
        )
        viewModelScope.launch(Dispatchers.IO) {
            userProfileInteractor.createUserProfile(user = user)
        }
    }
}