package com.example.hikeculator.presentation.member_management

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.User

class MemberManagementItemDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}