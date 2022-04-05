package com.example.hikeculator.presentation.general_trip_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemTripBinding
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.presentation.common.TripDateFormat
import com.example.hikeculator.presentation.common.getAnimated

class GeneralTripAdapter(
    private val onItemClick: (tripId: String) -> Unit,
    private val onLongItemClick: (trip: Trip) -> Unit,
) : ListAdapter<Trip, GeneralTripAdapter.TripViewHolder>(TripItemDiffCallback()) {

    private var rootRecyclerView: RecyclerView? = null
    private var recyclerWasNotAnimated = true

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rootRecyclerView = recyclerView
        recyclerWasNotAnimated = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTripBinding.inflate(inflater, parent, false)
        return TripViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trip = getItem(position))
    }

    override fun submitList(list: List<Trip>?) {
        super.submitList(list)

        if (recyclerWasNotAnimated) {
            rootRecyclerView?.apply {
                getAnimated(
                    layoutAnimationId = R.anim.recycler_view_general_trip_list_layout_animation
                )
                recyclerWasNotAnimated = false
            }
        }
    }

    inner class TripViewHolder(
        private val binding: ItemTripBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(trip: Trip) {

            with(binding) {
                textViewTripName.text = trip.name
                textViewMemberCount.text = trip.memberUids.size.toString()
                textViewStartDate.text = TripDateFormat.toFormattedDate(time = trip.startDate)
                textViewEndDate.text = TripDateFormat.toFormattedDate(trip.endDate)

                root.setOnClickListener { onItemClick.invoke(getItem(absoluteAdapterPosition).id) }

                root.setOnLongClickListener {
                    onLongItemClick(getItem(absoluteAdapterPosition))
                    true
                }
            }
        }
    }
}