package com.example.hikeculator.presentation.trip_creating

import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class TripDatePicker( private val onPositiveButtonClick: (pickedData: Pair<Long, Long>) -> Unit) {

    companion object {

        private const val TAG = "tag"
    }

    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private val today = MaterialDatePicker.todayInUtcMilliseconds()
    private val calendarConstraints = CalendarConstraints.Builder()
        .setStart(today)
        .setOpenAt(today)
        .build()

    private val picker = MaterialDatePicker.Builder.dateRangePicker()
        .setTitleText("Select the trip date")
//                .setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
//                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
        .setCalendarConstraints(calendarConstraints)
        .build()

    init {
        picker.addOnPositiveButtonClickListener { pickedDate -> onPositiveButtonClick(pickedDate) }
    }

    fun show(manager: FragmentManager) {
        picker.show(manager, TAG)
    }
}