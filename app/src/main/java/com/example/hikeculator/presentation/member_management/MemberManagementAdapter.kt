package com.example.hikeculator.presentation.member_management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemMemberBinding
import com.example.hikeculator.domain.entities.User

class MemberManagementAdapter(
    private val onDeleteItemClick: (member: User) -> Unit
) : ListAdapter<User, MemberManagementAdapter.MemberItemViewHolder>(
    MemberManagementItemDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member, parent, false)
        return MemberItemViewHolder(binding = ItemMemberBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {
        holder.bind(user = getItem(holder.absoluteAdapterPosition))
    }

    inner class MemberItemViewHolder(
        val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.textViewUserName.text = user.name
            binding.textViewEmail.text = user.email

            binding.buttonDeleteItem.setOnClickListener { onDeleteItemClick(user) }
        }
    }
}