package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.GroupMemberRepository

class GroupMemberInteractor(private val groupMemberRepository: GroupMemberRepository) {

    fun addTripMember(user: User) {
        groupMemberRepository.addTripMember(user)
    }

    fun removeTripMember(user: User) {
        groupMemberRepository.removeTripMember(user)
    }

    fun fetchTripMember(): User {
        return groupMemberRepository.fetchTripMember()
    }

    fun fetchTripMembers(): Set<User> {
        return groupMemberRepository.fetchTripMembers()
    }
}