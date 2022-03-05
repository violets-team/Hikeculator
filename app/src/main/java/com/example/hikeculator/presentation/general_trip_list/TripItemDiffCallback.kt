package com.example.hikeculator.presentation.general_trip_list

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.Trip

class TripItemDiffCallback : DiffUtil.ItemCallback<Trip>() {

    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem == newItem
}