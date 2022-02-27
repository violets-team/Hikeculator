package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.USER_COLLECTION_NAME
import com.example.hikeculator.data.entities.FirestoreUser
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImpl : UserProfileRepository {

    private val firestore = Firebase.firestore

    override suspend fun fetchUser(userUid: String): FirestoreUser? {
        return firestore.collection(USER_COLLECTION_NAME)
            .document(userUid)
            .get()
            .await()
            .toObject<FirestoreUser>()
    }

    override suspend fun createUserProfile(user: FirestoreUser) {
        firestore.collection(USER_COLLECTION_NAME).document(user.uid).set(user).await()
    }
}