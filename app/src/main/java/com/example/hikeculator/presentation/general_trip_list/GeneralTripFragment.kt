package com.example.hikeculator.presentation.general_trip_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentGeneralTripsBinding
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class GeneralTripFragment : Fragment(R.layout.fragment_general_trips) {

    private val binding by viewBinding(FragmentGeneralTripsBinding::bind)

    private val args by navArgs<GeneralTripFragmentArgs>()
    private val navController by lazy { findNavController() }

    private val viewModel by viewModel<GeneralTripViewModel>()

    private val tripAdapter = GeneralTripAdapter(
        onItemClick = ::navigateToTripDetailFragment,
        onLongItemClick = ::deleteTrip
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeTripRecyclerView()
        observeTripListSate()

        binding.actionButtonCreateTrip.setOnClickListener { navigateToTripCreatingFragment() }

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

    private fun observeTripListSate() {
        viewModel.trips.collectWhenStarted(lifecycleScope) { trips ->
            tripAdapter.submitList(trips.toList())
        }

        viewModel.problemMessage.collectWhenStarted(lifecycleScope) { messageId ->
            requireContext().showToast(messageId = messageId)
        }
    }

    private fun deleteTrip(trip: Trip) {
        viewModel.deleteTrip(trip = trip)
    }

    private fun navigateToTripDetailFragment(tripId: String) {
        GeneralTripFragmentDirections.actionGeneralTripFragmentToTripDetailFragment(
            tripId = tripId
        ).also { navController.navigate(directions = it) }
    }

    private fun navigateToTripCreatingFragment() {
        GeneralTripFragmentDirections.actionGeneralTripFragmentToTripCreatingFragment(
            userUid = args.userUid
        ).also { navController.navigate(directions = it) }
    }
}

