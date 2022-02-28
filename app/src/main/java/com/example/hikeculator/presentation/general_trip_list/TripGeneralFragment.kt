package com.example.hikeculator.presentation.general_trip_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentGeneralTripsBinding

class TripGeneralFragment: Fragment(R.layout.fragment_general_trips) {

    private val binding by viewBinding(FragmentGeneralTripsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actionButtonGeneralTripsCreateTrip.setOnClickListener {
           findNavController().navigate(R.id.action_hikesFragment_to_createHikeFragment)
        }

        binding.bottomAppBarGeneralTrips.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.app_bar_item_profile -> {
                    findNavController().navigate(R.id.action_hikesFragment_to_profileFragment)
                    true
                }
                else -> false
            }
        }
    }
}

