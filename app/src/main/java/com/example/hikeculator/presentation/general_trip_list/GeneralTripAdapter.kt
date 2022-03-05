package com.example.hikeculator.presentation.general_trip_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.databinding.ItemTripBinding
import com.example.hikeculator.domain.entities.Trip

class GeneralTripAdapter(
    private val onItemClick: (tripId: String) -> Unit,
    private val onLongItemClick: (tripId: String) -> Unit,
) : ListAdapter<Trip, GeneralTripAdapter.TripViewHolder>(TripItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTripBinding.inflate(inflater, parent, false)
        return TripViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trip = getItem(position))
    }

    inner class TripViewHolder(
        private val binding: ItemTripBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(trip: Trip) {
            with(binding) {
                val dates = "${trip.startDate} - ${trip.endDate}"
                textViewTripName.text = trip.name
                textViewMemberCount.text = trip.memberCount.toString()
                textViewTripDates.text = dates

                root.setOnClickListener { onItemClick.invoke(getItem(absoluteAdapterPosition).id) }

                root.setOnLongClickListener {
                    onLongItemClick(getItem(absoluteAdapterPosition).id)
                    true
                }
            }
        }
    }
}