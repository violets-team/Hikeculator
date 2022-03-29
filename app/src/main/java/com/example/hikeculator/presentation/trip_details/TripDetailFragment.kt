package com.example.hikeculator.presentation.trip_details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentTripDetailsBinding
import com.example.hikeculator.domain.common.*
import com.example.hikeculator.domain.common.NutritionalCalculator.getCarbNorm
import com.example.hikeculator.domain.common.NutritionalCalculator.getFatNorm
import com.example.hikeculator.domain.common.NutritionalCalculator.getProteinsNorm
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.showToast
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TripDetailFragment : Fragment(R.layout.fragment_trip_details) {

    private val binding by viewBinding(FragmentTripDetailsBinding::bind)
    private val args by navArgs<TripDetailFragmentArgs>()

    private val navController by lazy { findNavController() }

    private val viewModel by viewModel<TripDetailViewModel> { parametersOf(args.tripId) }

    private val tripDetailDayAdapter = TripDetailDayAdapter(onItemClick = ::navigateToTriDayDetails)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeTripDayRecyclerView()
        observeTripDetailState()
        setListeners()
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

    private fun observeTripDetailState() {
        viewModel.data.collectWhenStarted(lifecycleScope) { data: Pair<Trip?, List<TripDay>> ->
            data.first?.let { trip ->
                tripDetailDayAdapter.submitList(data.second)
                binding.setViewContent(trip = trip, tripDays = data.second)
            }
        }

        viewModel.problemMessage.collectWhenStarted(lifecycleScope) { messageId ->
            requireContext().showToast(messageId = messageId)
        }
    }

    private fun setListeners() {
        binding.tripDetailButtons.setOnProvisionBagButtonClickListener {
            navigateToProvisionBag(tripId = args.tripId)
        }

        binding.tripDetailButtons.setOnMemberButtonClickListener { navigateToMembers() }
    }

    private fun navigateToTriDayDetails(dayId: String) {
        navController.navigate(R.id.action_tripDetailFragment_to_tripDayDetailFragment)
    }

    private fun navigateToProvisionBag(tripId: String) {
        TripDetailFragmentDirections.actionTripDetailFragmentToProvisionBagFragment(
            tripId = tripId
        ).also { navController.navigate(it) }
    }

    private fun navigateToMembers() {
        navController.navigate(R.id.action_tripDetailFragment_to_memberManagementFragment)
    }

    private fun FragmentTripDetailsBinding.setViewContent(trip: Trip, tripDays: List<TripDay>) {
        val tripCalories = trip.totalCalories

        displayTripName(tripName = trip.name)

        displayCalorieInfo(
            calorieNorm = tripCalories,
            provisionCalories = tripDays.getProvisionCalories()
        )

        displayProteinInfo(
            proteinNorm = getProteinsNorm(calories = tripCalories),
            provisionProteins = tripDays.getProvisionProteinAmount()
        )

        displayFatInfo(
            fatNorm = getFatNorm(calories = tripCalories),
            provisionFats = tripDays.getProvisionFatAmount()
        )

        displayCarbInfo(
            carbNorm = getCarbNorm(calories = tripCalories),
            provisionCarbs = tripDays.getProvisionCarbsAmount()
        )

        displayProvisionWeight(weight = tripDays.getProvisionWeight())
    }

    private fun FragmentTripDetailsBinding.displayTripName(tripName: String) {
        textViewTripNameTitle.text = tripName
    }

    private fun FragmentTripDetailsBinding.displayCalorieInfo(
        calorieNorm: Long,
        provisionCalories: Long,
    ) {
        displayNutritionInfo(
            nutritionNorm = calorieNorm,
            provisionNutritionValue = provisionCalories,
            nutritionInfoTextView = textViewProvisionCalorieInfo,
            nutritionInfoProgressIndicator = progressIndicatorCalorieInfo
        )

        textViewTripCalories.text = calorieNorm.toString()
    }

    private fun FragmentTripDetailsBinding.displayProteinInfo(
        proteinNorm: Long,
        provisionProteins: Long,
    ) {
        displayNutritionInfo(
            nutritionNorm = proteinNorm,
            provisionNutritionValue = provisionProteins,
            nutritionInfoTextView = textViewProteinInfo,
            nutritionInfoProgressIndicator = progressIndicatorProteinInfo
        )
    }

    private fun FragmentTripDetailsBinding.displayFatInfo(fatNorm: Long, provisionFats: Long) {
        displayNutritionInfo(
            nutritionNorm = fatNorm,
            provisionNutritionValue = provisionFats,
            nutritionInfoTextView = textViewFatInfo,
            nutritionInfoProgressIndicator = progressIndicatorFatInfo
        )
    }

    private fun FragmentTripDetailsBinding.displayCarbInfo(carbNorm: Long, provisionCarbs: Long) {
        displayNutritionInfo(
            nutritionNorm = carbNorm,
            provisionNutritionValue = provisionCarbs,
            nutritionInfoTextView = textViewCarbInfo,
            nutritionInfoProgressIndicator = progressIndicatorCarbInfo
        )
    }

    private fun displayNutritionInfo(
        nutritionNorm: Long,
        provisionNutritionValue: Long,
        nutritionInfoTextView: TextView,
        nutritionInfoProgressIndicator: CircularProgressIndicator,
    ) {
        val nutritionPercent = provisionNutritionValue percentageOf nutritionNorm

        nutritionInfoTextView.text = getString(R.string.pfc_persents, nutritionPercent)
        nutritionInfoProgressIndicator.progress = nutritionPercent
    }

    private fun FragmentTripDetailsBinding.displayProvisionWeight(weight: Long) {
        textViewTripProvisionWeight.text = getString(R.string.text_trip_provision_weight, weight)
    }
}