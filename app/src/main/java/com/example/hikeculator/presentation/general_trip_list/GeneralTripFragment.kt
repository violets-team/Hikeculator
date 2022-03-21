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
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.showToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GeneralTripFragment : Fragment(R.layout.fragment_general_trips) {

    private val binding by viewBinding(FragmentGeneralTripsBinding::bind)

    private val args by navArgs<GeneralTripFragmentArgs>()
    private val navController by lazy { findNavController() }

    private val tripDayRepository by inject<TripDayRepository> { parametersOf(args.userUid) }
    private val tripDayInteractor by inject<TripDayInteractor>{ parametersOf(tripDayRepository) }

    private val tripInteractor by inject<TripInteractor> { parametersOf(args.userUid) }
    private val viewModel by viewModel<GeneralTripViewModel> {
        parametersOf(tripInteractor, tripDayInteractor)
    }

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

    private fun  observeTripListSate() {
        viewModel.tripData.collectWhenStarted(lifecycleScope) { trips ->
            tripAdapter.submitList(trips.toList())
        }

        viewModel.problemMessage.collectWhenStarted(lifecycleScope) { messageId ->
            requireContext().showToast(messageId = messageId)
        }
    }

    private fun deleteTrip(tripId: String) {
        viewModel.deleteTrip(userUid = args.userUid, tripId = tripId)
    }

    private fun navigateToTripDetailFragment(tripId: String) {
        GeneralTripFragmentDirections.actionGeneralTripFragmentToTripDetailFragment(
            userUid = args.userUid,
            tripId = tripId
        ).also { navController.navigate(directions = it) }
    }

    private fun navigateToTripCreatingFragment() {
        GeneralTripFragmentDirections.actionGeneralTripFragmentToTripCreatingFragment(
            userUid = args.userUid
        ).also { navController.navigate(directions = it) }
    }
}

