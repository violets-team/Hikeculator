package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.UserProfileRepository

class UserProfileInteractor(private val userProfileRepository: UserProfileRepository) {

    suspend fun fetchUserProfile(): User = userProfileRepository.fetchUserProfile()

    suspend fun createUserProfile(user: com.example.hikeculator.domain.entities.User) {
        userProfileRepository.createUserProfile(user = user)
    }
}