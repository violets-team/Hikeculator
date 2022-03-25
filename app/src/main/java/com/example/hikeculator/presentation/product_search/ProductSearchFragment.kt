package com.example.hikeculator.presentation.product_search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentProductSearchBinding
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductSearchFragment : Fragment(R.layout.fragment_product_search) {

    private val viewModel by viewModel<ProductSearchViewModel>()

    private val viewBinding by viewBinding(FragmentProductSearchBinding::bind)

    private val searchedProductsAdapter = ProductSearchAdapter()

    private val args by navArgs<ProductSearchFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSearchRecyclerView()
        initializeFlowCollectors()
        collectData()
    }

    private fun initializeSearchRecyclerView() {
        viewBinding.recyclerViewListOfProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchedProductsAdapter
        }
    }

    private fun initializeFlowCollectors() {
        viewModel.productSearchResult.collectWhenStarted(lifecycleScope = lifecycleScope) { products ->
            searchedProductsAdapter.submitList(products)
            viewBinding.recyclerViewListOfProducts.smoothScrollToPosition(0)
            viewBinding.progressBarSearch.visibility = View.GONE
        }
        viewModel.searchError.collectWhenStarted(lifecycleScope = lifecycleScope) { stringResId ->
            showSnackBar(stringResId)
        }
    }

    private fun collectData() {
        viewBinding.editTextSearch.addTextChangedListener { text ->
            searchProducts(text.toString())
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

    private fun searchProducts(searchExpression: String) {
        viewBinding.progressBarSearch.visibility = View.VISIBLE
        viewModel.search(searchExpression)
    }

    private fun showSnackBar(resId: Int) =
        Snackbar.make(viewBinding.root, getString(resId), Snackbar.LENGTH_SHORT).show()
}