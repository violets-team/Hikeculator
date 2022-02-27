package com.example.hikeculator.data.common

import com.example.hikeculator.data.entities.FirestoreTrip
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.data.entities.FirestoreUser
import com.example.hikeculator.domain.entities.User

fun FirestoreTrip.mapToTrip() = Trip(
    id = id,
    name = name,
    startDate = startDate,
    endDate = endDate,
    memberCount = memberCount,
    totalCalories = totalCalories,
    type = type,
    difficultyCategory = difficultyCategory,
    season = season
)

fun Trip.mapToFirestoreTrip(): FirestoreTrip = FirestoreTrip(
    id = id,
    name = name,
    startDate = startDate,
    endDate = endDate,
    memberCount = memberCount,
    totalCalories = totalCalories,
    type = type,
    difficultyCategory = difficultyCategory,
    season = season
)
  
fun User.mapToFirestoreUser(uid: String, token: String) = FirestoreUser(
    uid = uid,
    token = token,
    name = name,
    email = email,
    password = email,
    age = age,
    weight = weight,
    height = height,
    gender = gender,
    calorieNorm = calorieNorm
)