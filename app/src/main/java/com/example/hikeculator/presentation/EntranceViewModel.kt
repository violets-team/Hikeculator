package com.example.hikeculator.presentation

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EntranceViewModel(
    private val firebase: Firebase,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    fun getUserUid(): String? = firebase.auth.currentUser?.uid

    fun isCurrentUserSignedIn(): Boolean = firebase.auth.currentUser != null

    fun getSignedInUserUid(): String? = firebaseAuth.currentUser?.uid
}