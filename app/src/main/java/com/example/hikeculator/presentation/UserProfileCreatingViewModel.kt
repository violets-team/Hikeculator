package com.example.hikeculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.data.common.mapToFirestoreUser
import com.example.hikeculator.data.repository_implementations.UserProfileRepositoryImpl
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import com.example.hikeculator.domain.repositories.UserProfileRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileCreatingViewModel : ViewModel() {

    private val userProfileRepository: UserProfileRepository = UserProfileRepositoryImpl()
    private val userProfileInteractor = UserProfileInteractor(userProfileRepository)

    fun createUser(
        uid: String,
        token: String,
        name: String,
        email: String,
        password: String,
        age: Int,
        weight: Double,
        height: Double,
        gender: Gender,
    ) {
        val calorieNorm = NutritionalCalculator().calculateCalorieNorm()

        val user = User(
            name = name,
            email = email,
            password = password,
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
            user.mapToFirestoreUser(uid = uid, token = token).also { firebaseUser ->
                userProfileInteractor.createUserProfile(user = firebaseUser)
            }
        }
    }
}