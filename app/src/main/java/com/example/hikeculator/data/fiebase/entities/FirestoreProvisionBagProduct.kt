package com.example.hikeculator.data.fiebase.entities

import com.example.hikeculator.data.fiebase.DEFAULT_BOOLEAN_VALUE
import com.example.hikeculator.data.fiebase.DEFAULT_LONG_VALUE
import com.example.hikeculator.data.fiebase.DEFAULT_STRING_VALUE

data class FirestoreProvisionBagProduct(
    val name: String = DEFAULT_STRING_VALUE,
    val weight: Long = DEFAULT_LONG_VALUE,
    val nutritionalValue: FirestoreNutritionValue = FirestoreNutritionValue(),
    @field:JvmField
    val isBought: Boolean = DEFAULT_BOOLEAN_VALUE,
    val id: String = DEFAULT_STRING_VALUE
) {

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FirestoreProvisionBagProduct

        if (id != other.id) return false

        return true
    }
}