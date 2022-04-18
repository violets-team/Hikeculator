package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface MemberGroupRepository {

    suspend fun addTripMember(tripId: String, vararg userUids: String)

    suspend fun removeTripMember(tripId: String, userUid: String)

    suspend fun fetchTripMembers(tripId: String): Set<User>

    suspend fun fetchTripMember(userUid: String): User?

    suspend fun searchTripMembers(email: String): Set<User>
}