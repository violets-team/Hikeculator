package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import kotlinx.coroutines.flow.Flow


interface UserProfileRepository {

    fun fetchObservableUser(userUid: String): Flow<User?>

    suspend fun fetchUser(userUid: String): User?

    suspend fun createUserProfile(user: User)

    suspend fun isUserCreated(userUid: String): Boolean

    suspend fun updateUserProfile(
        user: User,
        name: String? = null,
        email: String? = null,
        age: Int? = null,
        weight: Double? = null,
        height: Int? = null,
        gender: Gender? = null,
        calorieNorm: Long? = null,
        tripIds: Set<String>? = null,
    )

    suspend fun addTripIdToUserProfile(userUid: String, tripId: String)

    suspend fun removeTripIdFromUserProfile(userUid: String, tripId: String)
}