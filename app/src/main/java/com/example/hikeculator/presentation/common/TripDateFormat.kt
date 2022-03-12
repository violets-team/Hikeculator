package com.example.hikeculator.presentation.common

import java.text.SimpleDateFormat
import java.util.*

object TripDateFormat {

    private const val DATE_PATTERN = "dd-MM-yy"

    private val format = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    fun toFormattedDate(time: Long): String = format.format(Date(time))
}