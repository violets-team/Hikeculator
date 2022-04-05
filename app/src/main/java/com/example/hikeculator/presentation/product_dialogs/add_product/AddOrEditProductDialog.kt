package com.example.hikeculator.presentation.product_dialogs.add_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentAddOrEditProductBinding
import com.example.hikeculator.domain.common.roundToTwoDecimalPlaces
import com.example.hikeculator.domain.entities.MealType.*
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.hideKeyBoardIfOpen
import com.example.hikeculator.presentation.common.onDone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddOrEditProductDialog : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentAddOrEditProductBinding::bind)

    private val args by navArgs<AddOrEditProductDialogArgs>()

    private val viewModel by viewModel<AddOrEditProductDialogViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_or_edit_product, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListenerExpandFragmentIfKeyBoardIsOpen()
        getSelectedProduct()
        setCancelTextViewListener()
    }

    private fun getSelectedProduct() {
        lifecycleScope.launch {
            viewModel.selectedProduct.collect { product ->
                setProductTitle(productName = product.name)
                setMealType()
                setWeightChangeListener()
                collectOnProductChanges()
                setButtonDoneListener()
                setSoftKeyBoardDoneListener()
            }
        }
    }

    private fun setCancelTextViewListener() {
        binding.textViewCancel.setOnClickListener {
            requireContext().hideKeyBoardIfOpen(binding.root)
            dismiss()
        }
    }

    private fun setListenerExpandFragmentIfKeyBoardIsOpen() {
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                sheet.parent.parent.requestLayout()
            }
        }
    }

    private fun addProductToTrip() {
        val textWeight = binding.editTextWeight.text.toString()
        val weight = textWeight.toLongOrNull()
        weight?.let {
            viewModel.addProductToTrip(
                tripId = args.tripId,
                dayId = args.dayId,
                mealType = args.mealType,
                weight = it
            )
        }
    }

    private fun collectOnProductChanges() {
        lifecycleScope.launch {
            viewModel.displayedProduct.collectWhenStarted(lifecycleScope) { product ->
                product.apply {
                    binding.setNutritionalValueContent(nutritional = nutritionalValue)
                }
            }
        }
    }

    private fun setWeightChangeListener() {
        binding.editTextWeight.doAfterTextChanged { weight ->
            val textWeight = weight.toString()
            if (textWeight.isNotEmpty()) {
                viewModel.setWeight(weight = textWeight.toLong())
            } else {
                viewModel.setWeight(weight = 0L)
            }
        }
    }

    private fun setMealType() {
        val mealType = when (args.mealType) {
            BREAKFAST -> getString(R.string.text_breakfast)
            LUNCH -> getString(R.string.text_lunch)
            DINNER -> getString(R.string.text_dinner)
            SNACK -> getString(R.string.text_snack)
        }
        binding.textViewDayMealType.text = mealType
    }

    private fun setButtonDoneListener() {
        binding.buttonDone.setOnClickListener {
            addProductToTrip()
            requireContext().hideKeyBoardIfOpen(binding.root)
            dismiss()
        }
    }

    private fun setSoftKeyBoardDoneListener() {
        binding.editTextWeight.onDone {
            addProductToTrip()
            requireContext().hideKeyBoardIfOpen(binding.root)
            dismiss()
        }
    }

    private fun setProductTitle(productName: String) {
        binding.textViewProductTitle.text = productName
    }

    private fun FragmentAddOrEditProductBinding.setNutritionalValueContent(nutritional: NutritionalValue) {
        nutritional.apply {
            textViewCalories.text = calories.roundToTwoDecimalPlaces().toString()
            textViewProteins.text = proteins.roundToTwoDecimalPlaces().toString()
            textViewFats.text = fats.roundToTwoDecimalPlaces().toString()
            textViewCarbs.text = carbs.roundToTwoDecimalPlaces().toString()
        }
    }
}