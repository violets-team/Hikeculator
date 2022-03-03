package com.example.hikeculator.presentation.trip_creating

import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.DialogFragmentTripSeasonChoosingBinding

class SeasonDialogFragment: DialogFragment(R.layout.dialog_fragment_trip_season_choosing) {

    private val binding by viewBinding(DialogFragmentTripSeasonChoosingBinding::bind)
}