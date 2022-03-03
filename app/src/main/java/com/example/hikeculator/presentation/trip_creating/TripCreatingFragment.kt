package com.example.hikeculator.presentation.trip_creating

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentTripCreatingBinding

class TripCreatingFragment: Fragment(R.layout.fragment_trip_creating) {

    private val binding by viewBinding(FragmentTripCreatingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextSeason.setOnClickListener {
            var dialogSeason = SeasonDialogFragment()
            dialogSeason.show(requireActivity().supportFragmentManager, "customDialogSeason")
        }
        binding.editTextLevel.setOnClickListener {
            var dialogLevel = CategoryDialogFragment()
            dialogLevel.show(requireActivity().supportFragmentManager, "customDialogLevel")
        }
    }
}