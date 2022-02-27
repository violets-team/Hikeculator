package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.repositories.AuthorizationRepository

class AuthorizationInteractor(private val authorizationRepository: AuthorizationRepository) {

    fun signOut() = authorizationRepository.signOut()
}