package com.example.hikeculator.data.common

import com.example.hikeculator.data.entities.FirestoreUser
import com.example.hikeculator.domain.entities.User

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