package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.MemberGroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberGroupInteractor(private val memberGroupRepository: MemberGroupRepository) {

    suspend fun addTripMember(tripId: String, vararg userUids: String) {
        withContext(Dispatchers.IO) {
            memberGroupRepository.addTripMember(tripId = tripId, userUids = userUids)
        }
    }

    suspend fun removeTripMember(tripId: String, userUid: String) {
        withContext(Dispatchers.IO) {
            memberGroupRepository.removeTripMember(tripId = tripId, userUid = userUid)
        }
    }

    suspend fun fetchTripMember(userUid: String): User? = withContext(Dispatchers.IO) {
        memberGroupRepository.fetchTripMember(userUid = userUid)
    }

    suspend fun fetchTripMembers(tripId: String): Set<User> = withContext(Dispatchers.IO) {
        memberGroupRepository.fetchTripMembers(tripId = tripId)
    }

    suspend fun searchTripMembers(email: String): Set<User> = withContext(Dispatchers.IO) {
        memberGroupRepository.searchTripMembers(email = email)
    }
}