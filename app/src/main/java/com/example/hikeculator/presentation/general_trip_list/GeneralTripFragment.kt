package com.example.hikeculator.presentation.general_trip_list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.data.repository_implementations.TripRepositoryImpl
import com.example.hikeculator.databinding.FragmentGeneralTripsBinding
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.enums.Seasons
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripRepository
import com.example.hikeculator.presentation.common.launchWhenStarted
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GeneralTripFragment : Fragment(R.layout.fragment_general_trips) {

    private val binding by viewBinding(FragmentGeneralTripsBinding::bind)
    private val args by navArgs<GeneralTripFragmentArgs>()

    private val tripRepository by inject<TripRepository> { parametersOf(args.userUid) }
    private val tripInteractor by inject<TripInteractor> { parametersOf(tripRepository) }
    private val viewModel by viewModel<GeneralTripViewModel> { parametersOf(tripInteractor) }

    private val navController by lazy { findNavController() }

    private val tripAdapter = GeneralTripAdapter(
        onItemClick = ::navigateToTripDetailFragment,
        onLongItemClick = ::deleteTrip
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeTripRecyclerView()

        viewModel.tripData.onEach { trips -> tripAdapter.submitList(trips.toList()) }
            .launchWhenStarted(lifecycleScope)

        binding.actionButtonCreateTrip.setOnClickListener {
            navController.navigate(R.id.action_generalTripFragment_to_tripCreatingFragment)
        }

        binding.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.app_bar_item_profile -> {
                    navController.navigate(R.id.action_generalTripFragment_to_profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun initializeTripRecyclerView() {
        binding.recyclerViewTrips.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            adapter = tripAdapter
        }
    }

    private fun deleteTrip(tripId: String) {
        viewModel.deleteTrip(tripId = tripId)
    }

    private fun navigateToTripDetailFragment(tripId: String) {
        GeneralTripFragmentDirections.actionGeneralTripFragmentToTripDetailFragment(
            userUid = args.userUid,
            tripId = tripId
        ).also { navController.navigate(directions = it) }
    }
}

