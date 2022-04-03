package com.example.hikeculator.presentation.product_search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
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
import com.google.android.material.snackbar.Snackbar
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductSearchFragment : Fragment(R.layout.fragment_product_search) {

    private val binding by viewBinding(FragmentProductSearchBinding::bind)

    private val navController by lazy { findNavController() }
    private val args by navArgs<ProductSearchFragmentArgs>()

    private val viewModel by viewModel<ProductSearchViewModel> { parametersOf(args.tripId)}

    private val searchedProductsAdapter = ProductSearchAdapter(::showAddOrEditProductDialog)

    private var keyBoardShowListener: Unregistrar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSearchRecyclerView()
        collectErrors()
        collectSearchResult()
        setEditTextListeners()
        setSoftKeyShowListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        keyBoardShowListener?.unregister()
    }

    private fun setSoftKeyShowListener() {
        keyBoardShowListener = KeyboardVisibilityEvent.registerEventListener(activity) {
            when (it) {
                true -> { binding.groupProductStatics.visibility = GONE }
                false -> { binding.groupProductStatics.visibility = VISIBLE }
            }
        }
    }

    private fun initializeSearchRecyclerView() {
        binding.recyclerViewListOfProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchedProductsAdapter
        }
    }


    private fun collectErrors() {
        viewModel.searchError.collectWhenStarted(lifecycleScope) { stringResId ->
            showSnackBar(stringResId)
        }
    }

    private fun collectSearchResult() {
        viewModel.productSearchResult.collectWhenStarted(lifecycleScope) { products ->
            searchedProductsAdapter.submitList(products)
            binding.progressBarSearch.visibility = GONE
        }
    }

    private fun setEditTextListeners() {
        binding.editTextSearch.addTextChangedListener { text ->
            searchProducts(text.toString())
        }

        binding.editTextSearch.setOnEditorActionListener { textView, actionId, _ ->
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
        binding.progressBarSearch.visibility = VISIBLE
        viewModel.search(searchExpression)
    }

    private fun showAddOrEditProductDialog(product: Product) {
        viewModel.saveSelectedProduct(product = product)
        hideKeyBoardIfOpened()
        navigateToAddOrEditDialog()
    }

    private fun hideKeyBoardIfOpened() {
        activity?.currentFocus.let { view ->
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun showSnackBar(resId: Int) =
        Snackbar.make(binding.root, getString(resId), Snackbar.LENGTH_SHORT).show()

    private fun navigateToAddOrEditDialog() {
        ProductSearchFragmentDirections.actionProductSearchFragmentToAddOrEditProductDialog(
            tripId = args.tripId,
            dayId = args.dayId,
            args.mealType,
        ).also { navController.navigate(directions = it) }
    }
}