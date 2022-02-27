package com.example.hikeculator.presentation.create_trip

import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ChooseSeasonCreateTripDialogBinding
import com.example.hikeculator.databinding.FragmentCreateTripBinding

class SeasonDialogFragment: DialogFragment(R.layout.choose_season_create_trip_dialog) {

    private val binding by viewBinding(ChooseSeasonCreateTripDialogBinding::bind)
}