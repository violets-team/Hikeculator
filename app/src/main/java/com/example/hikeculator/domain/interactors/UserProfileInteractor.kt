package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.example.hikeculator.domain.repositories.UserUidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserProfileInteractor(
    private val userProfileRepository: UserProfileRepository,
    private val userUidRepository: UserUidRepository
) {

    fun fetchObservableUserProfile(): Flow<User?> {
        return userProfileRepository.fetchObservableUser(userUid = userUidRepository.uid)
    }

    suspend fun createUserProfile(user: User) {
        withContext(Dispatchers.IO) { userProfileRepository.createUserProfile(user = user) }
    }

    suspend fun isUserCreated(userUid: String): Boolean {
        return userProfileRepository.isUserCreated(userUid = userUid)
    }
}