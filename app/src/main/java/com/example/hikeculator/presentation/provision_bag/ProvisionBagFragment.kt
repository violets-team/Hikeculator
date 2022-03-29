package com.example.hikeculator.presentation.provision_bag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentProvisionBagBinding
import com.example.hikeculator.presentation.common.collectWhenStarted
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProvisionBagFragment: Fragment(R.layout.fragment_provision_bag) {

    private val binding by viewBinding(FragmentProvisionBagBinding::bind)

    private val args by navArgs<ProvisionBagFragmentArgs>()

    private val viewModel by viewModel<ProvisionBagViewModel> { parametersOf(args.tripId) }

    private val provisionBagAdapter = ProvisionBagAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        viewModel.products.collectWhenStarted(lifecycleScope) { products ->
            provisionBagAdapter.updateData(data = products.toList())
        }
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = provisionBagAdapter
        }
    }
}