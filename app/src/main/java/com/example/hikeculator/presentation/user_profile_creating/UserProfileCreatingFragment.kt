package com.example.hikeculator.presentation.user_profile_creating

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentUserProfileCreatingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileCreatingFragment : Fragment(R.layout.fragment_user_profile_creating) {

    private val binding by viewBinding(FragmentUserProfileCreatingBinding::bind)
    private val args by navArgs<UserProfileCreatingFragmentArgs>()

    private val viewModel by viewModel<UserProfileCreatingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonCreate.setOnClickListener {
                viewModel.createUser(
                    uid = args.userUid,
                    name = editTextProfileName.text.toString().trim(),
                    email = args.userEmail,
                    age = editTextAge.text.toString().trim().toInt(),
                    weight = editTextWeight.text.toString().trim().toDouble(),
                    height = editTextProfileHeight.text.toString().trim().toInt(),
                    isMan = isMan()
                )

                navigateToGeneralTripFragment(userUid = args.userUid)
            }
        }
    }

    private fun isMan(): Boolean = binding.radioButtonMan.isChecked

    private fun navigateToGeneralTripFragment(userUid: String) {
        UserProfileCreatingFragmentDirections
            .actionUserProfileCreatingFragmentToGeneralTripFragment(userUid = userUid)
            .also { findNavController().navigate(directions = it) }
    }
}