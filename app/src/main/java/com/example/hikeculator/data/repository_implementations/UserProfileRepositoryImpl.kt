package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.firestore_constants.USER_COLLECTION_NAME
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImpl : UserProfileRepository {

    private val firestore = Firebase.firestore

    override suspend fun fetchUserProfile(): User {
        TODO("Not yet implemented")
    }

    override suspend fun createUserProfile(user: User) {
        firestore.collection(USER_COLLECTION_NAME).add(user).await()
    }
}