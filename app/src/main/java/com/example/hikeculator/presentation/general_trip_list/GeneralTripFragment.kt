package com.example.hikeculator.presentation.general_trip_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.data.repository_implementations.UserProfileRepositoryImpl
import com.example.hikeculator.databinding.FragmentGeneralTripsBinding
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripRepository
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

        viewModel.tripData.collectWhenStarted(lifecycleScope) { trips ->
            tripAdapter.submitList(trips.toList())

            lifecycleScope.launch {
                UserProfileRepositoryImpl(Firebase.firestore, FirebaseAuth.getInstance())
                    .fetchUser(userUid = "i63ONyptCDNxjvJMHnAJZbYanId2")?.let { user ->
                        Log.i("app_log",
                            "onViewCreated: ${user.calorieNorm}"
                        )

//                        trips.onEach { trip ->
//                            Log.i("app_log", "onViewCreated:" +
//                                    "\n  ${trip}" +
//                                    "\n trip cal ->${NutritionalCalculator().calculateCalorieNorm(trip, user)}" +
//                                    "\n ******************************"
//                            )
//                        }
                    }
            }

        }

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

    private fun deleteTrip(tripId: String) {
        viewModel.deleteTrip(tripId = tripId)
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

