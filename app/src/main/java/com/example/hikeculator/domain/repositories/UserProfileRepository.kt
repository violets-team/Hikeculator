package com.example.hikeculator.domain.repositories

import com.example.hikeculator.data.entities.FirestoreUser


interface UserProfileRepository {

    suspend fun fetchUser(userUid: String): FirestoreUser?

    suspend fun createUserProfile(user: FirestoreUser)
}