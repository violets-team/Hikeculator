package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.User

interface GroupMemberRepository {

    fun addTripMember(user: User)

    fun removeTripMember(user: User)

    fun fetchTripMembers(): Set<User>

    fun fetchTripMember(): User
}