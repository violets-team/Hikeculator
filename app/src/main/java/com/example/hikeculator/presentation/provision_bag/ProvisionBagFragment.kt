package com.example.hikeculator.presentation.provision_bag

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentProvisionBagBinding
import com.example.hikeculator.domain.entities.ProvisionBagProduct
import com.example.hikeculator.presentation.common.RecyclerViewAnimator
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProvisionBagFragment : Fragment(R.layout.fragment_provision_bag) {

    private val binding by viewBinding(FragmentProvisionBagBinding::bind)

    private val args by navArgs<ProvisionBagFragmentArgs>()

    private val viewModel by viewModel<ProvisionBagViewModel> { parametersOf(args.tripId) }

    private val provisionBagAdapter = ProvisionBagAdapter(onItemClick = ::updateProduct)
    private val recyclerViewAnimator by lazy {
        RecyclerViewAnimator(recyclerView = binding.recyclerViewProvisionBag)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()
        observeProvisionBagState()
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewProvisionBag.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = provisionBagAdapter

            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.recycler_view_provision_bag_layout_animation
            )
        }
    }

    private fun observeProvisionBagState() {
        viewModel.problemMessage.collectWhenStarted(lifecycleScope) { stringId ->
            requireContext().showToast(messageId = stringId)
        }

        viewModel.products.collectWhenStarted(lifecycleScope) { products ->
            provisionBagAdapter.submitList(products.toList())
            recyclerViewAnimator.animateOnlyOnce()
        }

        viewModel.tripName.collectWhenStarted(lifecycleScope) { tripName ->
            binding.textViewTripName.text = tripName
        }
    }

    private fun updateProduct(product: ProvisionBagProduct) {
        viewModel.updatedProduct(updatedProduct = product)
    }
}