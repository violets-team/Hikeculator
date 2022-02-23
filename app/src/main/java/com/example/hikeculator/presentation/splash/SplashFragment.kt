package com.example.hikeculator.presentation.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentSplashBinding


class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.startInit()
    }

    private fun observeViewModel() {
        viewModel.status.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_splashFragment_to_hikesFragment)
            }
        }
    }
}