package com.example.hikeculator.data.common

import com.example.hikeculator.data.fiebase.entities.*
import com.example.hikeculator.data.retrofit.WEIGHT_UNIT
import com.example.hikeculator.data.retrofit.entities.ApiNutritionalValue
import com.example.hikeculator.data.retrofit.entities.ApiProductHolder
import com.example.hikeculator.data.retrofit.entities.FoodSearchResponse
import com.example.hikeculator.domain.common.divideByOneHundred
import com.example.hikeculator.domain.entities.*

fun FirestoreTrip.mapToTrip() = Trip(
    id = id,
    name = name,
    startDate = startDate,
    endDate = endDate,
    memberUids = memberUids.toSet(),
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
    memberUids = memberUids.toList(),
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

fun FirestoreUser.mapToUser() = User(
    uid = uid,
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
    date = date,
    breakfast = breakfast.mapToFirestoreDayMeal(),
    lunch = lunch.mapToFirestoreDayMeal(),
    dinner = dinner.mapToFirestoreDayMeal(),
    snack = snack.mapToFirestoreDayMeal(),
)

fun FirestoreTripDay.mapToTripDay(): TripDay = TripDay(
    id = id,
    date = date,
    breakfast = breakfast.mapToDayMeal(),
    lunch = lunch.mapToDayMeal(),
    dinner = dinner.mapToDayMeal(),
    snack = snack.mapToDayMeal(),
)

fun ProvisionBag.mapToFirestoreProvisionBag() = FirestoreProvisionBag(
    productList = productList.toList()
)

fun FirestoreProvisionBag.mapToProvisionBag() = ProvisionBag(
    productList = productList.toSet()
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
        carbohydrates = carbs
    )
}

fun FirestoreNutritionValue.mapToNutritionalValue(): NutritionalValue = NutritionalValue(
    calories = calories,
    proteins = proteins,
    fats = fats,
    carbs = carbohydrates
)

fun FoodSearchResponse.mapToProductList(): List<Product> =
    productHolders.map { productHolder -> productHolder.mapToProduct() }


fun ApiProductHolder.mapToProduct() = Product(
    id = product.id,
    name = product.name,
    weight = WEIGHT_UNIT,
    nutritionalValue = product.nutritionalValue.mapToNutritionalValue(),
)

fun ApiNutritionalValue.mapToNutritionalValue() = NutritionalValue(
    calories = calories.divideByOneHundred(),
    proteins = proteins.divideByOneHundred(),
    fats = fats.divideByOneHundred(),
    carbs = carbohydrates.divideByOneHundred()
)
