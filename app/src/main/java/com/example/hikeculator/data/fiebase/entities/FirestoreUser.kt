package com.example.hikeculator.data.fiebase.entities

import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.enums.Gender.*

data class FirestoreUser(
    val uid: String = DEFAULT_STRING_VALUE,
    val token: String? = DEFAULT_STRING_VALUE,
    val name: String = DEFAULT_STRING_VALUE,
    val email: String = DEFAULT_STRING_VALUE,
    val age: Int = DEFAULT_INT_VALUE,
    val weight: Double = DEFAULT_DOUBLE_VALUE,
    val height: Int = DEFAULT_INT_VALUE,
    val gender: Gender = MAN,
    val calorieNorm: Long = DEFAULT_LONG_VALUE,
) {
    companion object {
        private const val DEFAULT_STRING_VALUE: String = ""
        private const val DEFAULT_DOUBLE_VALUE: Double = -1.0
        private const val DEFAULT_INT_VALUE: Int = -1
        private const val DEFAULT_LONG_VALUE: Long = -1
    }
}