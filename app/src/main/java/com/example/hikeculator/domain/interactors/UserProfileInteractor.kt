package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.firestore.auth.User

class UserProfileInteractor(private val userProfileRepository: UserProfileRepository) {

    fun fetchUserProfile(): User {
        return userProfileRepository.fetchUserProfile()
    }
}