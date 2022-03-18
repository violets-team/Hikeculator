package com.example.hikeculator.presentation.trip_details

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.TripDay

class TripDayItemDiffCallback : DiffUtil.ItemCallback<TripDay>() {

    override fun areItemsTheSame(oldItem: TripDay, newItem: TripDay): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TripDay, newItem: TripDay): Boolean {
        return oldItem == newItem
    }
}