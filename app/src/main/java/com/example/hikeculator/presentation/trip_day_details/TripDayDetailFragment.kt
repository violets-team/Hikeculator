package com.example.hikeculator.presentation.trip_day_details

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentTripDayDetailsBinding
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.common.percentageOf
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.enums.MealType.*
import com.example.hikeculator.presentation.common.RecyclerViewAnimator
import com.example.hikeculator.presentation.common.TripDateFormat
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TripDayDetailFragment : Fragment(R.layout.fragment_trip_day_details) {

    private val binding by viewBinding(FragmentTripDayDetailsBinding::bind)

    private val navController by lazy { findNavController() }
    private val args by navArgs<TripDayDetailFragmentArgs>()

    private val viewModel by viewModel<TripDayDetailViewModel> {
        parametersOf(args.tripId, args.dayId)
    }

    private val breakfastFoodAdapter =
        TripDayFoodAdapter(mealType = BREAKFAST, onClickDeleteItem = ::deleteFood)
    private val lunchFoodAdapter =
        TripDayFoodAdapter(mealType = LUNCH, onClickDeleteItem = ::deleteFood)
    private val dinnerFoodAdapter =
        TripDayFoodAdapter(mealType = DINNER, onClickDeleteItem = ::deleteFood)
    private val snackFoodAdapter =
        TripDayFoodAdapter(mealType = SNACK, onClickDeleteItem = ::deleteFood)

    private val breakfastRecyclerViewAnimator by lazy {
        RecyclerViewAnimator(recyclerView = binding.recyclerViewBreakfastOverview)
    }
    private val lunchRecyclerViewAnimator by lazy {
        RecyclerViewAnimator(recyclerView = binding.recyclerViewLunchOverview)
    }
    private val dinnerRecyclerViewAnimator by lazy {
        RecyclerViewAnimator(recyclerView = binding.recyclerViewDinnerOverview)
    }
    private val snackRecyclerViewAnimator by lazy {
        RecyclerViewAnimator(recyclerView = binding.recyclerViewSnackOverview)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAddMealClickListeners()
        initializeRecyclerViews()
        setListeners()
        observeTripDayData()
    }

    private fun deleteFood(food: Product, dayMealType: MealType) {
        viewModel.deleteFood(food = food, mealType = dayMealType)
    }

    private fun initializeRecyclerViews() {
        with(binding) {
            recyclerViewBreakfastOverview.initialize(adapter = breakfastFoodAdapter)
            recyclerViewLunchOverview.initialize(adapter = lunchFoodAdapter)
            recyclerViewDinnerOverview.initialize(adapter = dinnerFoodAdapter)
            recyclerViewSnackOverview.initialize(adapter = snackFoodAdapter)
        }
    }

    private fun RecyclerView.initialize(adapter: TripDayFoodAdapter) {
        layoutManager = LinearLayoutManager(requireContext())
        this.adapter = adapter

        layoutAnimation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.recycler_view_trip_day_meal_layout_animation
        )
    }

    private fun setListeners() {
        with(binding) {
            textViewBreakfast.setOnClickListener {
                manageDisplayingRecyclerView(
                    recyclerView = recyclerViewBreakfastOverview,
                    recyclerViewAnimator = breakfastRecyclerViewAnimator
                )
            }

            textViewLunch.setOnClickListener {
                manageDisplayingRecyclerView(
                    recyclerView = recyclerViewLunchOverview,
                    recyclerViewAnimator = lunchRecyclerViewAnimator
                )
            }

            textViewDinner.setOnClickListener {
                manageDisplayingRecyclerView(
                    recyclerView = recyclerViewDinnerOverview,
                    recyclerViewAnimator = dinnerRecyclerViewAnimator
                )
            }

            textViewSnack.setOnClickListener {
                manageDisplayingRecyclerView(
                    recyclerView = recyclerViewSnackOverview,
                    recyclerViewAnimator = snackRecyclerViewAnimator
                )
            }
        }
    }

    private fun observeTripDayData() {
        viewModel.tripData.collectWhenStarted(lifecycleScope) { data: Pair<Trip?, TripDay?> ->
            val trip = data.first
            val tripDay = data.second

            if (trip != null && tripDay != null) {
                binding.setViewContent(trip = trip, tripDay = tripDay)

                breakfastFoodAdapter.submitList(tripDay.breakfast.products)
                lunchFoodAdapter.submitList(tripDay.lunch.products)
                dinnerFoodAdapter.submitList(tripDay.dinner.products)
                snackFoodAdapter.submitList(tripDay.snack.products)
            }
        }
    }

    private fun manageDisplayingRecyclerView(
        recyclerView: RecyclerView,
        recyclerViewAnimator: RecyclerViewAnimator
    ) {
        with(recyclerView) {
            visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE

            if (visibility == View.VISIBLE) {
                if (!recyclerViewAnimator.wasNotAnimated) {
                    recyclerViewAnimator.reset()
                }

                recyclerViewAnimator.animateOnlyOnce()
            } else {
                recyclerViewAnimator.reset()
            }
        }
    }

    private fun setAddMealClickListeners() {
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


    private fun FragmentTripDayDetailsBinding.setViewContent(
        trip: Trip,
        tripDay: TripDay
    ) {
        val dayCalorieNorm = NutritionalCalculator.getDayCaloriesNorm(trip = trip)

        displayTripDayNote(tripName = trip.name)
        displayTripDayOrdinal(trip = trip, tripDay = tripDay)
        displayTripDayDate(tripDay = tripDay)

        displayCalorieInfo(
            calorieNorm = dayCalorieNorm,
            provisionCalories = tripDay.getTotalCalories()
        )

        displayProteinInfo(
            proteinNorm = NutritionalCalculator.getProteinsNorm(calories = dayCalorieNorm),
            provisionProteins = NutritionalCalculator.getDayProteinAmountPerCalories(tripDay = tripDay)
        )

        displayFatInfo(
            fatNorm = NutritionalCalculator.getFatNorm(calories = dayCalorieNorm),
            provisionFats = NutritionalCalculator.getDayFatAmountPerCalories(tripDay = tripDay)
        )

        displayCarbInfo(
            carbNorm = NutritionalCalculator.getCarbNorm(calories = dayCalorieNorm),
            provisionCarbs = NutritionalCalculator.getDayCarbAmountPerCalories(tripDay = tripDay)
        )

    }

    private fun displayTripDayNote(tripName: String) {
        binding.textViewTripDayNote.text = getString(R.string.text_trip_day_note, tripName)
    }

    private fun displayTripDayOrdinal(trip: Trip, tripDay: TripDay) {
        val dayOrdinal =
            TripDateFormat.getTripDayOrdinal(startDate = trip.startDate, dayDay = tripDay.date)

        binding.textViewDayTitle.text = getString(R.string.text_day_ordinal, dayOrdinal)
    }

    private fun displayTripDayDate(tripDay: TripDay) {
        binding.textViewDate.text = TripDateFormat.toFormattedDate(time = tripDay.date)
    }

    private fun FragmentTripDayDetailsBinding.displayCalorieInfo(
        calorieNorm: Double,
        provisionCalories: Double,
    ) {
        displayNutritionInfo(
            nutritionNorm = calorieNorm,
            provisionNutritionValue = provisionCalories,
            nutritionInfoTextView = textViewProvisionCalorieInfo,
            nutritionInfoProgressIndicator = progressIndicatorCalorieInfo
        )

        textViewTripCalories.text = calorieNorm.toLong().toString()
    }

    private fun FragmentTripDayDetailsBinding.displayProteinInfo(
        proteinNorm: Double,
        provisionProteins: Double,
    ) {
        displayNutritionInfo(
            nutritionNorm = proteinNorm,
            provisionNutritionValue = provisionProteins,
            nutritionInfoTextView = textViewProteinInfo,
            nutritionInfoProgressIndicator = progressIndicatorProteinInfo
        )
    }

    private fun FragmentTripDayDetailsBinding.displayFatInfo(
        fatNorm: Double,
        provisionFats: Double
    ) {
        displayNutritionInfo(
            nutritionNorm = fatNorm,
            provisionNutritionValue = provisionFats,
            nutritionInfoTextView = textViewFatInfo,
            nutritionInfoProgressIndicator = progressIndicatorFatInfo
        )
    }

    private fun FragmentTripDayDetailsBinding.displayCarbInfo(
        carbNorm: Double,
        provisionCarbs: Double,
    ) {
        displayNutritionInfo(
            nutritionNorm = carbNorm,
            provisionNutritionValue = provisionCarbs,
            nutritionInfoTextView = textViewCarbInfo,
            nutritionInfoProgressIndicator = progressIndicatorCarbInfo
        )
    }

    private fun displayNutritionInfo(
        nutritionNorm: Double,
        provisionNutritionValue: Double,
        nutritionInfoTextView: TextView,
        nutritionInfoProgressIndicator: CircularProgressIndicator,
    ) {
        val nutritionPercent = provisionNutritionValue percentageOf nutritionNorm

        nutritionInfoTextView.text = getString(R.string.pfc_percents, nutritionPercent)
        nutritionInfoProgressIndicator.progress = nutritionPercent
    }
}