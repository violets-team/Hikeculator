package com.example.hikeculator.presentation.trip_creating

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.data.repository_implementations.TripDayRepositoryImpl
import com.example.hikeculator.databinding.FragmentTripCreatingBinding
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripDifficultyCategory.*
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripSeason.*
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripDayRepository
import com.example.hikeculator.domain.repositories.TripRepository
import com.example.hikeculator.presentation.common.TripDateFormat
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.toTrimmed
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class TripCreatingFragment : Fragment(R.layout.fragment_trip_creating) {

    private val binding by viewBinding(FragmentTripCreatingBinding::bind)

    private val args by navArgs<TripCreatingFragmentArgs>()
    private val navController by lazy { findNavController() }

    private val tripRepository by inject<TripRepository>()
    private val tripInteractor by inject<TripInteractor> {
        parametersOf(args.userUid, tripRepository)
    }

    private val tripDayRepository by inject<TripDayRepository> { parametersOf(args.userUid) }
    private val tripDayInteractor by inject<TripDayInteractor> { parametersOf(tripDayRepository) }
    private val viewModel by sharedViewModel<ITripCreatingViewModel> {
        parametersOf(tripInteractor, tripDayInteractor, args.userUid)
    }

    private val addedMemberAdapter = AddedTripMemberAdapter(onRemoveItemClick = ::removeAddedMember)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMemberRecyclerView()
        observeTripCreatingState()
        setClickListeners()
    }

    private fun removeAddedMember(user: User) {
        viewModel.removeTripMember(member = user)
    }

    private fun initializeMemberRecyclerView() {
        binding.recyclerViewMembers.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = addedMemberAdapter
        }
    }

    private fun observeTripCreatingState() {
        viewModel.seasonState.collectWhenStarted(lifecycleScope) { season: TripSeason? ->
            binding.textViewSeason.text = getTripSeasonTextViewContent(season)
        }

        viewModel.categoryState.collectWhenStarted(lifecycleScope) { category: TripDifficultyCategory? ->
            binding.textViewDifficultyCategory.text = getTripCategoryTextViewContent(category)
        }

        viewModel.typeState.collectWhenStarted(lifecycleScope) { tripType: TripType? ->
            binding.textViewType.text = getTripTypeTextViewContent(tripType)
        }

        viewModel.dateState.collectWhenStarted(lifecycleScope) { date: Pair<Long, Long>? ->
            binding.textViewDate.text = getDateTextViewContent(date)
        }

        viewModel.addedMembers.collectWhenStarted(lifecycleScope) { addedMembers ->
            addedMemberAdapter.submitList(addedMembers.toList())
            binding.textViewMemberQuantity.text = "${addedMembers.size}"
        }

        viewModel.problemMessage.collectWhenStarted(lifecycleScope) { stringId ->
            getString(stringId)
        }
    }

    private fun setClickListeners() {
        binding.textViewSeason.setOnClickListener { navigateToSeasonDialog() }
        binding.textViewDifficultyCategory.setOnClickListener { navigateToCategoryDialog() }
        binding.textViewType.setOnClickListener { navigateToTripTypeDialog() }
        binding.buttonCreate.setOnClickListener { createTrip() }
        binding.groupMemberCount.setOnClickListener { navigateToMemberDialog() }
        binding.textViewDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        TripDatePicker(
            title = getString(R.string.date_picker_title_select_trip_date),
            onPositiveButtonClick = ::setTripDate
        ).show(childFragmentManager)
    }

    private fun setTripDate(pickedDate: androidx.core.util.Pair<Long, Long>) {
        viewModel.setDate(pickedDate = Pair(pickedDate.first, pickedDate.second))
    }

    private fun navigateToSeasonDialog() {
        navController.navigate(R.id.action_tripCreatingFragment_to_seasonDialogFragment)
    }

    private fun navigateToCategoryDialog() {
        navController.navigate(R.id.action_tripCreatingFragment_to_categoryDialogFragment)
    }

    private fun navigateToTripTypeDialog() {
        navController.navigate(R.id.action_tripCreatingFragment_to_tripTypeDialogFragment)
    }

    private fun navigateToMemberDialog() {
        navController.navigate(R.id.action_tripCreatingFragment_to_memberDialogFragment)
    }

    private fun createTrip() {
        val season = viewModel.seasonState.value
        val category = viewModel.categoryState.value
        val tripType = viewModel.typeState.value
        val date = viewModel.dateState.value

        when {
            binding.editTextName.toTrimmed()
                .isEmpty() -> showToast(R.string.trip_name_typing_action)
            season == null -> showToast(R.string.trip_season_choosing_action)
            tripType == null -> showToast(R.string.trip_type_choosing_action)
            category == null -> showToast(R.string.trip_category_choosing_action)
            date == null -> showToast(R.string.trip_date_picking_action)
            else -> {
                viewModel.createTrip(
                    name = binding.editTextName.toTrimmed(),
                    startDate = date.first,
                    endDate = date.second,
                    type = tripType,
                    difficultyCategory = category,
                    season = season
                )

                navController.popBackStack()
            }
        }
    }

    private fun getTripSeasonTextViewContent(season: TripSeason?): String {
        val descriptionId = when (season) {
            SPRING -> R.string.spring_enum_description
            SUMMER -> R.string.summer_enum_description
            FALL -> R.string.fall_enum_description
            WINTER -> R.string.winter_enum_desctiption
            null -> R.string.trip_season_choosing_action
        }
        return getString(descriptionId)
    }

    private fun getTripCategoryTextViewContent(category: TripDifficultyCategory?): String {
        val description = when (category) {
            CATEGORY_1 -> R.string.category_1_enam_description
            CATEGORY_2 -> R.string.category_2_enum_description
            CATEGORY_3 -> R.string.category_3_enum_description
            CATEGORY_4 -> R.string.category_4_enum_description
            CATEGORY_5 -> R.string.category_5_enum_description
            CATEGORY_6 -> R.string.category_6_enum_description
            null -> R.string.trip_category_choosing_action
        }
        return getString(description)
    }

    private fun getTripTypeTextViewContent(tripType: TripType?): String {
        val description = when (tripType) {
            TripType.HIKE -> R.string.hike_type_enum_description
            TripType.SKI -> R.string.ski_type_enum_description
            TripType.WATER -> R.string.water_type_enum_description
            TripType.MOUNTAIN -> R.string.mountain_type_enum_description
            null -> R.string.trip_type_choosing_action
        }
        return getString(description)
    }

    private fun getDateTextViewContent(date: Pair<Long, Long>?): String {
        return if (date == null) {
            getString(R.string.date_text_view_pick_action)
        } else {
            val startDate = TripDateFormat.toFormattedDate(date.first)
            val endDate = TripDateFormat.toFormattedDate(date.second)
            "$startDate - $endDate"
        }
    }

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}