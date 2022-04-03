package com.example.hikeculator.presentation.trip_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemDayBinding
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.presentation.common.TripDateFormat
import com.example.hikeculator.presentation.common.getAnimated

class TripDetailDayAdapter(
    private val onItemClick: (dayId: String) -> Unit,
) : ListAdapter<TripDay, TripDetailDayAdapter.TripDayViewHolder>(TripDayItemDiffCallback()) {

    companion object {

        private const val DAY_ORDINAL_CORRECTION_FACTOR = 1
    }

    private var rootRecyclerView: RecyclerView? = null
    private var recyclerWasNotAnimated = true

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rootRecyclerView = recyclerView
        recyclerWasNotAnimated = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayBinding.inflate(inflater, parent, false)
        return TripDayViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: TripDayViewHolder, position: Int) {
        holder.bind()
    }

    override fun submitList(list: List<TripDay>?) {
        super.submitList(list)

        if (recyclerWasNotAnimated) {
            rootRecyclerView?.apply {
                getAnimated(
                    layoutAnimationId = R.anim.recycler_view_trip_details_layout_animation
                )
                recyclerWasNotAnimated = false
            }
        }
    }

    inner class TripDayViewHolder(
        val binding: ItemDayBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            with(binding) {
                val day = getItem(absoluteAdapterPosition)
                val dayOrdinal = absoluteAdapterPosition + DAY_ORDINAL_CORRECTION_FACTOR
                val dayNumberText = root.context.getString(R.string.text_item_day, dayOrdinal)
                val date = TripDateFormat.toFormattedDate(day.date)

                textViewTripDayNumber.text = dayNumberText
                textViewTripDate.text = date

                root.setOnClickListener { onItemClick(day.id) }
            }
        }
    }
}