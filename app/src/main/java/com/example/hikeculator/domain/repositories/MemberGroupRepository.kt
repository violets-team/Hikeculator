package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface MemberGroupRepository {

    fun addTripMember(user: User)

    fun removeTripMember(user: User)

    fun fetchTripMembers(): Flow<Set<User>>

    fun fetchTripMember(): User

    suspend fun searchTripMembers(email: String): Set<User>
}