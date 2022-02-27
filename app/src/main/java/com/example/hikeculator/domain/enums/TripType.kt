package com.example.hikeculator.domain.enums

enum class TripType(val factor: Double) {

    HIKE(factor = 1.0),
    SKI(factor = 1.2),
    WATER(factor = 0.8),
    MOUNTAIN(factor = 1.3),
}