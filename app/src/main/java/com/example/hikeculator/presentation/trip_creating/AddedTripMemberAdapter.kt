package com.example.hikeculator.presentation.trip_creating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.databinding.ItemAddedTripMemberBinding
import com.example.hikeculator.domain.entities.User

class AddedTripMemberAdapter(
    private val onRemoveItemClick: (member: User) -> Unit,
) : ListAdapter<User, AddedTripMemberAdapter.AddedMemberViewHolder>(
    AddedTripMemberItemDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddedTripMemberBinding.inflate(inflater, parent, false)
        return AddedMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddedMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddedMemberViewHolder(
        val binding: ItemAddedTripMemberBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            with(binding) {
                textViewMemberName.text = user.name
                textViewMemberEmail.text = user.email

                imageViewRemoveMember.setOnClickListener {
                    onRemoveItemClick(getItem(absoluteAdapterPosition))
                }
            }
        }
    }
}