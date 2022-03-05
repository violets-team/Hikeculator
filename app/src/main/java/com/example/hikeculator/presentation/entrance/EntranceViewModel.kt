package com.example.hikeculator.presentation.entrance

import androidx.lifecycle.ViewModel
import com.example.hikeculator.data.entities.FirebaseAuthentication
import com.example.hikeculator.domain.interactors.UserProfileInteractor

class EntranceViewModel(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val userProfileInteractor: UserProfileInteractor
) : ViewModel() {

    fun getUserUid(): String? = firebaseAuthentication.getUserUid()

    fun getUserEmail(): String? = firebaseAuthentication.getUserEmail()

    fun isCurrentUserSignedIn(): Boolean = firebaseAuthentication.isCurrentUserSignedIn()

    fun getSignedInUserUid(): String? = firebaseAuthentication.getSignedInUserUid()

    fun getSignedInUserEmail(): String? = firebaseAuthentication.getSignedInUserEmail()

    suspend fun isUserCreated(userUid: String): Boolean {
        return userProfileInteractor.isUserCreated(userUid = userUid)
    }
}