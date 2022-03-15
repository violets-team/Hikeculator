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
import com.example.hikeculator.databinding.DialogFragmentTripCategoryChoosingBinding
import com.example.hikeculator.domain.enums.TripDifficultyCategory.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CategoryDialogFragment : DialogFragment(R.layout.dialog_fragment_trip_category_choosing) {

    private val binding by viewBinding(DialogFragmentTripCategoryChoosingBinding::bind)

    private val viewModel by sharedViewModel<ITripCreatingViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonConfirm.setOnClickListener {
                val category = when {
                    radioButtonCategory1.isChecked -> CATEGORY_1
                    radioButtonCategory2.isChecked -> CATEGORY_2
                    radioButtonCategory3.isChecked -> CATEGORY_3
                    radioButtonCategory4.isChecked -> CATEGORY_4
                    radioButtonCategory5.isChecked -> CATEGORY_5
                    else -> CATEGORY_6
                }

                viewModel.setCategory(category)
                findNavController().popBackStack()
            }
        }
    }

}