package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.USER_COLLECTION_NAME
import com.example.hikeculator.data.common.mapToFirestoreUser
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImpl : UserProfileRepository {

    private val firestore = Firebase.firestore

    override suspend fun fetchUser(userUid: String): User? {
        return firestore.collection(USER_COLLECTION_NAME)
            .document(userUid)
            .get()
            .await()
            .toObject<User>()
    }

    override suspend fun createUserProfile(user: User) {
        val token = FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(true)
            ?.await()
            ?.token

        user.mapToFirestoreUser(token = token).also { firestoreUser ->
            firestore.collection(USER_COLLECTION_NAME)
                .document(firestoreUser.uid)
                .set(firestoreUser)
                .await()
        }
    }
}