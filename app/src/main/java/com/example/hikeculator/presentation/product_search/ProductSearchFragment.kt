package com.example.hikeculator.presentation.product_search

import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentProductSearchBinding
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.hideKeyBoardIfOpen
import com.example.hikeculator.presentation.common.onDone
import com.example.hikeculator.presentation.common.setTextPercentage
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductSearchFragment : Fragment(R.layout.fragment_product_search) {

    private val binding by viewBinding(FragmentProductSearchBinding::bind)

    private val navController by lazy { findNavController() }
    private val args by navArgs<ProductSearchFragmentArgs>()

    private val viewModel by viewModel<ProductSearchViewModel> {
        parametersOf(
            args.tripId,
            args.dayId,
            args.mealType
        )
    }

    private val searchedProductsAdapter = ProductSearchAdapter(
        onItemClicked = ::showAddOrEditProductDialog
    )

    private val autoCompleteAdapter: AutoCompleteAdapter by lazy {
        AutoCompleteAdapter(requireContext(), android.R.layout.simple_list_item_1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSearchAutoCompleteAdapter()
        setOnAutoCompleteItemClickListener()
        initializeSearchRecyclerView()
        collectData()
        setOnEditTextListeners()
    }

    private fun initializeSearchAutoCompleteAdapter() {
        binding.editTextSearch.setAdapter(autoCompleteAdapter)
    }

    private fun setOnAutoCompleteItemClickListener() {
        binding.editTextSearch.setOnItemClickListener { _, _, position, _ ->
            searchProducts(searchExpression = autoCompleteAdapter.getSuggestion(position))
            requireContext().hideKeyBoardIfOpen(binding.root)
        }
    }

    private fun initializeSearchRecyclerView() {
        binding.recyclerViewListOfProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchedProductsAdapter
        }
    }

    private fun collectData() {
        collectErrors()
        collectProductStatistics()
        collectAutoCompleteResult()
        collectSearchResult()
    }

    private fun setOnEditTextListeners() {
        binding.editTextSearch.doAfterTextChanged { text ->
            viewModel.autoComplete(text.toString())
        }

        binding.editTextSearch.onDone {
            searchProducts(binding.editTextSearch.text.toString())
            requireContext().hideKeyBoardIfOpen(binding.root)
            binding.editTextSearch.dismissDropDown()
        }
    }

    private fun collectErrors() {
        viewModel.errors.collectWhenStarted(lifecycleScope) { stringResId ->
            showSnackBar(stringResId)
        }
    }

    private fun collectProductStatistics() {
        viewModel.productStatistics.collectWhenStarted(lifecycleScope) { statistics ->
            binding.apply {
                progressIndicatorCaloriesInfo.progress = statistics.percentageOfCalories
                progressIndicatorProteinsInfo.progress = statistics.percentageOfProteins
                progressIndicatorFatsInfo.progress = statistics.percentageOfFats
                progressIndicatorCarbsInfo.progress = statistics.percentageOfCarbs

                textViewCaloriesInfo.setTextPercentage(statistics.percentageOfCalories)
                textViewProteinsInfo.setTextPercentage(statistics.percentageOfProteins)
                textViewFatsInfo.setTextPercentage(statistics.percentageOfFats)
                textViewCarbsInfo.setTextPercentage(statistics.percentageOfCarbs)
            }
        }
    }

    private fun collectAutoCompleteResult() {
        viewModel.autoCompleteList.collectWhenStarted(lifecycleScope) { hints ->
            if (binding.editTextSearch.text.isEmpty()) {
                binding.editTextSearch.dismissDropDown()
            } else {
                autoCompleteAdapter.setSuggestions(hints)
                autoCompleteAdapter.filter.filter(binding.editTextSearch.text)
            }
        }
    }

    private fun collectSearchResult() {
        viewModel.productSearchResult.collectWhenStarted(lifecycleScope) { products ->
            searchedProductsAdapter.submitList(products)
            binding.progressBarSearch.visibility = GONE
        }
    }

    private fun searchProducts(searchExpression: String) {
        binding.progressBarSearch.visibility = VISIBLE
        viewModel.search(searchExpression)
    }

    private fun showAddOrEditProductDialog(product: Product) {
        viewModel.saveSelectedProduct(product = product)
        requireContext().hideKeyBoardIfOpen(binding.root)
        navigateToAddOrEditDialog()
    }

    private fun showSnackBar(resId: Int) =
        Snackbar.make(binding.root, getString(resId), Snackbar.LENGTH_SHORT).show()

    private fun navigateToAddOrEditDialog() {
        ProductSearchFragmentDirections.actionProductSearchFragmentToAddOrEditProductDialog(
            tripId = args.tripId,
            dayId = args.dayId,
            mealType = args.mealType,
        ).also { navController.navigate(directions = it) }
    }
}