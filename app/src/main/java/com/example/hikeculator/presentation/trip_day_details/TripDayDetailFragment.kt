package com.example.hikeculator.presentation.trip_day_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentTripDayDetailsBinding
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.enums.MealType.*

class TripDayDetailFragment: Fragment(R.layout.fragment_trip_day_details) {

    private val binding by viewBinding(FragmentTripDayDetailsBinding::bind)

    private val navController by lazy { findNavController() }
    private val args by navArgs<TripDayDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMealClickListeners()
    }

    private fun setMealClickListeners() {
        binding.apply {
            buttonAddProductToBreakfast.setOnClickListener {
                navigateToSearchFragment(BREAKFAST)
            }
            buttonAddProductToLunch.setOnClickListener {
                navigateToSearchFragment(LUNCH)
            }
            buttonAddProductToDinner.setOnClickListener {
                navigateToSearchFragment(DINNER)
            }
            buttonAddProductToSnack.setOnClickListener {
                navigateToSearchFragment(SNACK)
            }
        }
    }

    private fun navigateToSearchFragment(mealType: MealType) {
        TripDayDetailFragmentDirections.actionTripDayDetailFragmentToProductSearchFragment(
            tripId = args.tripId,
            dayId = args.dayId,
            mealType = mealType
        ).also { navController.navigate(directions = it) }
    }
}