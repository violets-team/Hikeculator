package com.example.hikeculator.domain.interactors

import com.example.hikeculator.data.entities.FirestoreUser
import com.example.hikeculator.domain.repositories.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserProfileInteractor(private val userProfileRepository: UserProfileRepository) {

    suspend fun fetchUserProfile(userUid: String): FirestoreUser? = withContext(Dispatchers.IO) {
        userProfileRepository.fetchUser(userUid)
    }

    suspend fun createUserProfile(user: FirestoreUser) {
        withContext(Dispatchers.IO) { userProfileRepository.createUserProfile(user = user) }
    }
}