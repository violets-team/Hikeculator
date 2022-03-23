package com.example.hikeculator.presentation.user_profile_creating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.enums.Gender.MAN
import com.example.hikeculator.domain.enums.Gender.WOMAN
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileCreatingViewModel(
    private val userProfileInteractor: UserProfileInteractor,
) : ViewModel() {

    fun createUser(
        uid: String,
        name: String,
        email: String,
        age: Int,
        weight: Double,
        height: Int,
        isMan: Boolean,
    ) {
        val calorieNorm = NutritionalCalculator.calculateCalorieNorm(
            weight = weight,
            height = height,
            age = age,
            gender = getGender(isMan = isMan)
        )

        val user = User(
            uid = uid,
            name = name,
            email = email,
            age = age,
            weight = weight,
            height = height,
            gender = getGender(isMan = isMan),
            calorieNorm = calorieNorm
        )

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(context = Dispatchers.IO + exceptionHandler) {
            userProfileInteractor.createUserProfile(user = user)
        }
    }

    private fun getGender(isMan: Boolean): Gender = if (isMan) MAN else WOMAN
}