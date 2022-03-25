package com.example.hikeculator.presentation.entrance

import androidx.lifecycle.ViewModel
import com.example.hikeculator.data.fiebase.FirebaseAuthentication
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import com.example.hikeculator.domain.repositories.UserUidRepositiory

class EntranceViewModel(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val userProfileInteractor: UserProfileInteractor,
    private val userUidRepositiory: UserUidRepositiory,
) : ViewModel() {

    fun getUserUid(): String? = firebaseAuthentication.getUserUid()

    fun saveUserUid(uid: String) {
        userUidRepositiory.uid = uid
    }

    fun getUserEmail(): String? = firebaseAuthentication.getUserEmail()

    fun isCurrentUserSignedIn(): Boolean = firebaseAuthentication.isCurrentUserSignedIn()

    fun getSignedInUserUid(): String? = firebaseAuthentication.getSignedInUserUid()

    fun getSignedInUserEmail(): String? = firebaseAuthentication.getSignedInUserEmail()

    suspend fun isUserCreated(userUid: String): Boolean {
        return userProfileInteractor.isUserCreated(userUid = userUid)
    }
}