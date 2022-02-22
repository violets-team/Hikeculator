package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.User


interface UserProfileRepository {

    suspend fun fetchUserProfile(): User

    suspend fun createUserProfile(user: User)
}