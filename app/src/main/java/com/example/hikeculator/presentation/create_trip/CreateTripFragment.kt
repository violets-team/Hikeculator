package com.example.hikeculator.presentation.create_trip

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentCreateTripBinding

class CreateTripFragment: Fragment(R.layout.fragment_create_trip) {

    private val binding by viewBinding(FragmentCreateTripBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etCreateTripSeason.setOnClickListener {
            var dialogSeason = SeasonDialogFragment()
            dialogSeason.show(activity!!.supportFragmentManager, "customDialod")
        }
    }
}