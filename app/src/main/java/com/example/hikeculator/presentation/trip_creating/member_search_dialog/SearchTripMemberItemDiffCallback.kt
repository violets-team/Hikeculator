package com.example.hikeculator.presentation.trip_creating.member_search_dialog

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.User

class SearchTripMemberItemDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}