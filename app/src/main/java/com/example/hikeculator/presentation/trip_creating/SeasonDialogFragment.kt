package com.example.hikeculator.presentation.trip_creating

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.DialogFragmentTripSeasonChoosingBinding
import com.example.hikeculator.domain.enums.TripSeason.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SeasonDialogFragment : DialogFragment(R.layout.dialog_fragment_trip_season_choosing) {

    private val binding by viewBinding(DialogFragmentTripSeasonChoosingBinding::bind)

    private val viewModel by sharedViewModel<TripCreatingViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonConfirm.setOnClickListener {
                val season = when {
                    radioButtonSpring.isChecked -> SPRING
                    radioButtonSummer.isChecked -> SUMMER
                    radioButtonFall.isChecked -> FALL
                    radioButtonWinter.isChecked -> WINTER
                    else -> null
                }
                viewModel.setSeason(season)
                findNavController().popBackStack()
            }
        }
    }
}