package com.example.hikeculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import com.example.hikeculator.domain.repositories.UserProfileRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileCreatingViewModel(
    private val userProfileInteractor: UserProfileInteractor
) : ViewModel() {

    fun createUser(
        uid: String,
        name: String,
        email: String,
        age: Int,
        weight: Double,
        height: Double,
        gender: Gender,
    ) {
        val calorieNorm = NutritionalCalculator().calculateCalorieNorm()

        val user = User(
            uid = uid,
            name = name,
            email = email,
            age = age,
            weight = weight,
            height = height,
            gender = gender,
            calorieNorm = calorieNorm
        )

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            userProfileInteractor.createUserProfile(user = user)
        }
    }
}