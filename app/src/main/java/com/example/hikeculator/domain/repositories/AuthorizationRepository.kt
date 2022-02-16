package com.example.hikeculator.domain.repositories

interface AuthorizationRepository {

    fun signIn()

    fun logIn()

    fun signOut()
}