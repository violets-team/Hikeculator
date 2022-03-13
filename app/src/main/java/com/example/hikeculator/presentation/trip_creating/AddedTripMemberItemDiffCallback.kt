package com.example.hikeculator.presentation.trip_creating

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.User

class AddedTripMemberItemDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}