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

        binding.editTextTripCreatingSeason.setOnClickListener {
            var dialogSeason = SeasonDialogFragment()
            dialogSeason.show(activity!!.supportFragmentManager, "customDialod")
        }
    }
}