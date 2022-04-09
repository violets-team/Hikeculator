package com.example.hikeculator.data.repository_implementations

import android.util.Log
import com.example.hikeculator.data.common.getUserDocument
import com.example.hikeculator.data.common.mapToFirestoreUser
import com.example.hikeculator.data.common.mapToUser
import com.example.hikeculator.data.fiebase.entities.FirestoreUser
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserProfileRepository {

    override fun fetchObservableUser(userUid: String): Flow<User?> = callbackFlow {
        val listener = try {
            firestore.getUserDocument(userUid = userUid)
                .addSnapshotListener { documentSnapshot, error ->
                    if (error != null) {
                        close(cause = error)
                    } else {
                        documentSnapshot?.toObject<FirestoreUser>()
                            ?.mapToUser()
                            .also { user: User? -> trySend(element = user) }
                    }
                }

        } catch (e: Exception) {
            null
        }

        awaitClose { listener?.remove() }
    }

    override suspend fun fetchUser(userUid: String): User? {
        return firestore.getUserDocument(userUid)
            .get()
            .await()
            ?.toObject<FirestoreUser>()
            ?.mapToUser()
    }

    override suspend fun createUserProfile(user: User) {
        setUserToFirebase(user = user)
    }

    override suspend fun isUserCreated(userUid: String): Boolean {
        return firestore.getUserDocument(userUid)
            .get()
            .await()
            .exists()
    }

    override suspend fun updateUserProfile(
        user: User,
        name: String?,
        email: String?,
        age: Int?,
        weight: Double?,
        height: Int?,
        gender: Gender?,
        calorieNorm: Long?,
        tripIds: Set<String>?,
    ) {

        val updatedUser = user.copy(
            name = name ?: user.name,
            email = email ?: user.email,
            age = age ?: user.age,
            weight = weight ?: user.weight,
            gender = gender ?: user.gender,
            height = height ?: user.height,
            calorieNorm = calorieNorm ?: user.calorieNorm,
            tripIds = tripIds ?: user.tripIds,
        )

        setUserToFirebase(user = updatedUser)
    }

    override suspend fun addTripIdToUserProfile(userUid: String, tripId: String) {
        fetchUser(userUid = userUid)?.let { user ->
            user.copy(
                tripIds = listOf(* user.tripIds.toTypedArray(), tripId).toSet()
            ).also { updatedUser -> setUserToFirebase(user = updatedUser) }
        }
    }

    override suspend fun removeTripIdFromUserProfile(userUid: String, tripId: String) {
        fetchUser(userUid = userUid)?.let { user ->
            user.copy(
                tripIds = user.tripIds.toMutableList().apply { remove(tripId) }.toSet()
            ).also { updatedUser -> setUserToFirebase(user = updatedUser) }
        }
    }

    private suspend fun setUserToFirebase(user: User) {
        user.mapToFirestoreUser().also { firestoreUser ->
            firestore.getUserDocument(firestoreUser.uid)
                .set(firestoreUser)
                .await()
        }
    }
}