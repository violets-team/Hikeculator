package com.example.hikeculator.presentation.trip_creating.member_search_dialog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemSearchedTripMemberBinding
import com.example.hikeculator.domain.common.update
import com.example.hikeculator.domain.entities.User

class TripMemberSearchAdapter(
    private val onItemClick: (member: User) -> Unit,
) : ListAdapter<User, TripMemberSearchAdapter.SearchMemberViewHolder>(
    SearchTripMemberItemDiffCallback()
) {

    private val addedMembers = mutableListOf<User>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchedTripMemberBinding.inflate(inflater, parent, false)
        return SearchMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAddedMembers(date: List<User>) {
        addedMembers.update(newData = date)
        notifyDataSetChanged()
    }

    inner class SearchMemberViewHolder(
        val binding: ItemSearchedTripMemberBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            setBackground(isMemberAdded = addedMembers.contains(user))

            with(binding) {
                textViewMemberName.text = user.name
                textViewMemberEmail.text = user.email

                root.setOnClickListener { onItemClick(getItem(absoluteAdapterPosition)) }
            }
        }

        private fun setBackground(isMemberAdded: Boolean) {
            val drawableId = if (isMemberAdded) {
                R.drawable.background_item_searched_member_checked
            } else {
                R.drawable.background_item_searched_member_unchecked
            }

            binding.root.apply { background = ContextCompat.getDrawable(context, drawableId) }
        }
    }

}