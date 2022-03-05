package com.example.hikeculator.data.common

import com.example.hikeculator.data.entities.*
import com.example.hikeculator.domain.entities.*

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

fun User.mapToFirestoreUser(token: String?) = FirestoreUser(
    uid = uid,
    token = token,
    name = name,
    email = email,
    age = age,
    weight = weight,
    height = height,
    gender = gender,
    calorieNorm = calorieNorm
)

fun TripDay.mapToFirestoreTripDay(): FirestoreTripDay = FirestoreTripDay(
    id = id,
    breakfast = breakfast.mapToFirestoreDayMeal(),
    lunch = lunch.mapToFirestoreDayMeal(),
    dinner = dinner.mapToFirestoreDayMeal(),
    snack = snack.mapToFirestoreDayMeal(),
)

fun FirestoreTripDay.mapToTripDay(): TripDay = TripDay(
    id = id,
    breakfast = breakfast.mapToDayMeal(),
    lunch = lunch.mapToDayMeal(),
    dinner = dinner.mapToDayMeal(),
    snack = snack.mapToDayMeal(),
)

fun DayMeal.mapToFirestoreDayMeal(): FirestoreDayMeal = FirestoreDayMeal(
    products = products.map { product -> product.mapToFirestoreProduct() }
)

fun FirestoreDayMeal.mapToDayMeal(): DayMeal = DayMeal(
    products.map { firestoreProduct -> firestoreProduct.mapToProduct() }
)

fun Product.mapToFirestoreProduct(): FirestoreProduct = FirestoreProduct(
    name = name,
    weight = weight,
    nutritionalValue = nutritionalValue.mapToFirestoreNutritionalValue(),
)

fun FirestoreProduct.mapToProduct(): Product = Product(
    name = name,
    weight = weight,
    nutritionalValue = nutritionalValue.mapToNutritionalValue()
)

fun NutritionalValue.mapToFirestoreNutritionalValue(): FirestoreNutritionValue {
    return FirestoreNutritionValue(
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbohydrates = carbohydrates
    )
}

fun FirestoreNutritionValue.mapToNutritionalValue(): NutritionalValue = NutritionalValue(
    calories = calories,
    proteins = proteins,
    fats = fats,
    carbohydrates = carbohydrates
)