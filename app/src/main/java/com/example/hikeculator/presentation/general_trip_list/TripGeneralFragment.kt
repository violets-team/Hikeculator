package com.example.hikeculator.presentation.general_trip_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentGeneralTripBinding

class TripGeneralFragment: Fragment(R.layout.fragment_general_trip) {

    private val binding by viewBinding(FragmentGeneralTripBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCreateHike.setOnClickListener {
           findNavController().navigate(R.id.action_hikesFragment_to_createHikeFragment)
        }

        binding.bottomAppBarHikes.setOnMenuItemClickListener {
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

