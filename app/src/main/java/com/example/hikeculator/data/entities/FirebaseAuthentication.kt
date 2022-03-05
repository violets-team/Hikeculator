package com.example.hikeculator.data.entities

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthentication(
    private val firebase: Firebase,
    private val firebaseAuth: FirebaseAuth
) {

    fun getUserUid(): String? = firebase.auth.currentUser?.uid

    fun getUserEmail(): String? = firebase.auth.currentUser?.email

    fun isCurrentUserSignedIn(): Boolean = firebase.auth.currentUser != null

    fun getSignedInUserUid(): String? = firebaseAuth.currentUser?.uid

    fun getSignedInUserEmail(): String? = firebaseAuth.currentUser?.email
}