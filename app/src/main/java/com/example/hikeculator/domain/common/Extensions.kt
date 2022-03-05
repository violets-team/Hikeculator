package com.example.hikeculator.domain.common

fun <T> MutableList<T>.update(newData: List<T>) {
    clear()
    addAll(newData)
}