package com.example.hikeculator.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(val userProfileInteractor: UserProfileInteractor) : ViewModel() {

    val user: SharedFlow<User?> = userProfileInteractor.fetchObservableUserProfile().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

    private val _errors = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 0,
        BufferOverflow.DROP_OLDEST
    )
    val errors = _errors.asSharedFlow()

    fun updateUserProfile(name: String, weight: Double, height: Int, age: Int, gender: Gender) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _errors.tryEmit(R.string.text_error_saving_profile)
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val caloriesNorm = NutritionalCalculator.calculateCalorieNorm(
                weight = weight,
                height = height,
                age = age,
                gender = gender
            )

            getUserOrNull()?.copy(
                name = name,
                weight = weight,
                height = height,
                age = age,
                gender = gender,
                calorieNorm = caloriesNorm
            )?.let { userProfileInteractor.updateUserProfile(user = it) }
        }
    }

    private fun getUserOrNull(): User? {
        return if (user.replayCache.isNotEmpty()) {
            user.replayCache.first()
        } else {
            null
        }
    }
}
