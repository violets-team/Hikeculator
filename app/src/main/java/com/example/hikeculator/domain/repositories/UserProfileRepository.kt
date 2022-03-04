package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.User


interface UserProfileRepository {

    suspend fun fetchUser(userUid: String): User?

    suspend fun createUserProfile(user: User)

    suspend fun isUserCreated(userUid: String): Boolean
}