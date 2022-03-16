package com.example.hikeculator.presentation.common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TripDateFormat {

    private const val DAY_CORRECTION_FACTOR = 1

    private const val DATE_PATTERN = "dd-MM-yy"

    @SuppressLint("ConstantLocale")
    private val format = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    fun toFormattedDate(time: Long): String = format.format(Date(time))

    fun getDayQuantity(startDate: Long, endDate: Long): Int {
        return TimeUnit.MILLISECONDS.toDays(endDate - startDate)
            .toInt() + DAY_CORRECTION_FACTOR
    }
}