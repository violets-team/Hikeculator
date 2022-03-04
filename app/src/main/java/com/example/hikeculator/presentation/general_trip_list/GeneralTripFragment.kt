package com.example.hikeculator.presentation.general_trip_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentGeneralTripsBinding
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripRepository
import com.example.hikeculator.presentation.common.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GeneralTripFragment : Fragment(R.layout.fragment_general_trips) {

    private val binding by viewBinding(FragmentGeneralTripsBinding::bind)
    private val args by navArgs<GeneralTripFragmentArgs>()

    private val tripRepository by inject<TripRepository> { parametersOf(args.userUid) }
    private val tripInteractor by inject<TripInteractor> { parametersOf(tripRepository) }

    private val viewModel by viewModel<GeneralTripViewModel> { parametersOf(tripInteractor) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tripData.onEach { trips ->
            Log.i("app_log", "onViewCreated: $trips")
        }.launchWhenStarted(lifecycleScope)

        binding.actionButtonCreateTrip.setOnClickListener {
           findNavController().navigate(R.id.action_generalTripFragment_to_tripCreatingFragment)
        }

        binding.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.app_bar_item_profile -> {
                    findNavController().navigate(R.id.action_generalTripFragment_to_profileFragment)
                    true
                }
                else -> false
            }
        }
    }
}

