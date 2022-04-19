package com.example.hikeculator.presentation.product_dialogs.add_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentAddProductBinding
import com.example.hikeculator.domain.common.roundToTwoDecimalPlaces
import com.example.hikeculator.domain.enums.MealType.*
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

class AddProductDialog : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentAddProductBinding::bind)

    private val args by navArgs<AddProductDialogArgs>()

    private val viewModel by viewModel<AddProductDialogViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_product, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnKeyBoardOpeningListener()
        collectErrors()
        getSelectedProduct()
        setOnCancelClickListener()
    }

    private fun collectErrors() {
        viewModel.errors.collectWhenStarted(lifecycleScope) { dismiss() }
    }

    private fun getSelectedProduct() {
        lifecycleScope.launch {
            viewModel.selectedProduct.collect { product ->
                setProductTitle(productName = product.name)
                setMealTypeTitle()
                setOnWeightChangeListener()
                collectOnProductChanges()
                setOnButtonDoneListener()
                setKeyBoardDoneClickListener()
            }
        }
    }

    private fun setOnCancelClickListener() {
        binding.textViewCancel.setOnClickListener {
            requireContext().hideKeyBoardIfOpen(binding.root)
            dismiss()
        }
    }

    private fun setOnKeyBoardOpeningListener() {
        dialog?.setOnShowListener {
            expendFragment()
        }
    }

    private fun expendFragment() {
        val dialog = dialog as? BottomSheetDialog
        dialog?.findViewById<View>(R.id.design_bottom_sheet)?.let { sheet ->
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            sheet.parent.parent.requestLayout()
        }
    }

    private fun addProductToTrip() {
        val textWeight = binding.editTextWeight.text.toString()

        textWeight.toLongOrNull()?.let { weight ->
            viewModel.addProductToTrip(
                tripId = args.tripId,
                dayId = args.dayId,
                mealType = args.mealType,
                weight = weight
            )
        }
    }

    private fun collectOnProductChanges() {
        viewModel.displayedNutritionalValue.collectWhenStarted(lifecycleScope) { nutritionalValue ->
            binding.setNutritionalValueContent(nutritionalValue = nutritionalValue)
        }
    }

    private fun setOnWeightChangeListener() {
        binding.editTextWeight.doAfterTextChanged { weight ->
            val textWeight = weight.toString()
            if (textWeight.isNotEmpty()) {
                viewModel.setWeight(weight = textWeight.toLong())
            } else {
                viewModel.setWeight(weight = 0L)
            }
        }
    }

    private fun setMealTypeTitle() {
        val mealType = when (args.mealType) {
            BREAKFAST -> getString(R.string.text_breakfast)
            LUNCH -> getString(R.string.text_lunch)
            DINNER -> getString(R.string.text_dinner)
            SNACK -> getString(R.string.text_snack)
        }
        binding.textViewDayMealType.text = mealType
    }

    private fun setOnButtonDoneListener() {
        binding.buttonDone.setOnClickListener {
            addProductToTrip()
            requireContext().hideKeyBoardIfOpen(binding.root)
            dismiss()
        }
    }

    private fun setKeyBoardDoneClickListener() {
        binding.editTextWeight.onDone {
            addProductToTrip()
            requireContext().hideKeyBoardIfOpen(binding.root)
            dismiss()
        }
    }

    private fun setProductTitle(productName: String) {
        binding.textViewProductTitle.text = productName
    }

    private fun FragmentAddProductBinding.setNutritionalValueContent(nutritionalValue: NutritionalValue) {
        nutritionalValue.apply {
            textViewCalories.text = calories.roundToTwoDecimalPlaces().toString()
            textViewProteins.text = proteins.roundToTwoDecimalPlaces().toString()
            textViewFats.text = fats.roundToTwoDecimalPlaces().toString()
            textViewCarbs.text = carbs.roundToTwoDecimalPlaces().toString()
        }
    }
}