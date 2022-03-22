package com.example.hikeculator.presentation.food_search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentProductSearchBinding
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.google.android.material.snackbar.Snackbar

class ProductSearchFragment : Fragment(R.layout.fragment_product_search) {

    private val viewModel by viewModels<ProductSearchViewModel>()

    private val viewBinding by viewBinding(FragmentProductSearchBinding::bind)

    private val searchedProductsAdapter = ProductSearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSearchRecyclerView()
        initializeFlowCollectors()
        initializeSearchEditTextListeners()
    }

    private fun searchProducts(searchExpression: String) {
        viewBinding.progressBarSearch.visibility = View.VISIBLE
        viewModel.search(searchExpression)
    }

    private fun initializeFlowCollectors() {
        viewModel.productSearchResult.collectWhenStarted(lifecycleScope = lifecycleScope) { products ->
            searchedProductsAdapter.setSearchedList(products)
            viewBinding.progressBarSearch.visibility = View.GONE
        }
        viewModel.searchError.collectWhenStarted(lifecycleScope = lifecycleScope) { stringId ->
            Snackbar.make(viewBinding.root, getString(stringId), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initializeSearchEditTextListeners() {
        viewBinding.editTextSearch.addTextChangedListener { editable ->
           searchProducts(editable.toString())
        }
        viewBinding.editTextSearch.setOnEditorActionListener { textView, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchProducts(textView.text.toString())
                    true
                }
                else -> false
            }
        }
    }


    private fun initializeSearchRecyclerView() {
        viewBinding.recyclerViewListOfProducts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchedProductsAdapter
        }
    }
}