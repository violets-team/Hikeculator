package com.example.hikeculator.presentation.trip_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentTripDetailsBinding
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.example.hikeculator.presentation.common.collectWhenStarted
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TripDetailFragment : Fragment(R.layout.fragment_trip_details) {

    private val binding by viewBinding(FragmentTripDetailsBinding::bind)
    private val args by navArgs<TripDetailFragmentArgs>()

    private val tripInteractor by inject<TripInteractor> { parametersOf(args.userUid) }

    private val tripDayRepository by inject<TripDayRepository> {
        parametersOf(args.userUid, args.tripId)
    }
    private val tripDayInteractor by inject<TripDayInteractor> { parametersOf(tripDayRepository) }

    private val viewModel by viewModel<TripDetailViewModel> {
        parametersOf(tripInteractor, tripDayInteractor, args.tripId)
    }

    private val tripDetailDayAdapter = TripDetailDayAdapter(onItemClick = ::navigateToTriDayDetails)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeTripDayRecyclerView()

        viewModel.tripDays.collectWhenStarted(lifecycleScope) { tripDays ->
            tripDetailDayAdapter.submitList(tripDays)
        }

        viewModel.trip.collectWhenStarted(lifecycleScope) { trip: Trip? ->
            trip?.let {
                binding.setViewContent(trip = trip)
            }
        }
    }

    private fun initializeTripDayRecyclerView() {
        binding.recyclerViewTripDays.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = tripDetailDayAdapter
        }
    }

    private fun navigateToTriDayDetails(dayId: String) {
        findNavController().navigate(R.id.action_tripDetailFragment_to_tripDayDetailFragment)
    }

    private fun FragmentTripDetailsBinding.setViewContent(trip: Trip) {
        textViewTripNameTitle.text = trip.name
        textViewTripCalories.text = trip.totalCalories.toString()

        textViewTripProvisionWeight.text = getString(R.string.text_trip_provision_weight, 12343.3423)

        textViewGrainedCaloriePercents.text = getString(R.string.pfc_persents, 56)
        circularIndicatorCaloriesTrip.progress = 56

        textViewProteinInfo.text = getString(R.string.pfc_persents, 13)
        progressBarProteinInfo.progress = 13

        textViewFatInfo.text = getString(R.string.pfc_persents, 100)
        progressBarFatInfo.progress = 100

        textViewCarbInfo.text = getString(R.string.pfc_persents, 44)
        progressBarCarbInfo.progress = 44
    }
}