package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.MemberGroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MemberGroupInteractor(private val memberGroupRepository: MemberGroupRepository) {

    fun addTripMember(user: User) = memberGroupRepository.addTripMember(user)

    fun removeTripMember(user: User) = memberGroupRepository.removeTripMember(user)

    suspend fun fetchTripMember(userUid: String): User? = withContext(Dispatchers.IO) {
        memberGroupRepository.fetchTripMember(userUid = userUid)
    }

    fun fetchTripMembers(): Flow<Set<User>> = memberGroupRepository.fetchTripMembers()

    suspend fun searchTripMembers(email: String): Set<User> = withContext(Dispatchers.IO) {
        memberGroupRepository.searchTripMembers(email = email)
    }
}