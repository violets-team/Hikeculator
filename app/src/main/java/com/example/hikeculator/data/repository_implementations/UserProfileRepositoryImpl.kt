package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.*
import com.example.hikeculator.data.entities.FirestoreUser
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : UserProfileRepository {

    override suspend fun fetchUser(userUid: String): User? {
        return firestore.collection(USER_COLLECTION_NAME)
            .document(userUid)
            .get()
            .await()
            ?.toObject<FirestoreUser>()
            ?.mapToUser()
    }

    override suspend fun createUserProfile(user: User) {
        val token = firebaseAuth
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

    override suspend fun isUserCreated(userUid: String): Boolean {
        return firestore.collection(USER_COLLECTION_NAME)
            .document(userUid)
            .get()
            .await()
            .exists()
    }
}