package com.example.hikeculator.domain.enums

enum class TripSeason(val factor: Double) {

    SPRING(1.1),
    SUMMER(1.0),
    FALL(1.1),
    WINTER(1.2)
}