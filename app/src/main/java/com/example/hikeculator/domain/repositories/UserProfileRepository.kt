package com.example.hikeculator.domain.repositories

import com.google.firebase.firestore.auth.User

interface UserProfileRepository {

    fun fetchUserProfile(): User
}