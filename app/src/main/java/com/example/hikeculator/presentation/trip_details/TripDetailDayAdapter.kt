package com.example.hikeculator.presentation.trip_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemDayBinding
import com.example.hikeculator.domain.common.update
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.presentation.common.TripDateFormat

class TripDetailDayAdapter(
    private val onItemClick: (dayId: String) -> Unit,
) : ListAdapter<TripDay, TripDetailDayAdapter.TripDayViewHolder>(TripDayItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayBinding.inflate(inflater, parent, false)
        return TripDayViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: TripDayViewHolder, position: Int) {
        holder.bind()
    }

    inner class TripDayViewHolder(
        val binding: ItemDayBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            with(binding) {
                val day = getItem(absoluteAdapterPosition)
                val dayOrdinal = absoluteAdapterPosition + 1
                val dayNumberText = root.context.getString(R.string.text_item_day, dayOrdinal)
                val date = TripDateFormat.toFormattedDate(day.date)

                textViewTripDayNumber.text = dayNumberText
                textViewTripDate.text = date

                root.setOnClickListener { onItemClick(day.id) }
            }
        }
    }
}