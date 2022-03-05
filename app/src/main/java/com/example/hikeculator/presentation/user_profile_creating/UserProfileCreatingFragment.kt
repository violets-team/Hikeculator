package com.example.hikeculator.presentation.user_profile_creating

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
                verifyEditFields { areFieldsValid ->
                    if (areFieldsValid) {
                        viewModel.createUser(
                            uid = args.userUid,
                            name = editTextName.toTrimmed(),
                            email = args.userEmail,
                            age = editTextAge.toTrimmed().toInt(),
                            weight = editTextWeight.toTrimmed().toDouble(),
                            height = editTextHeight.toTrimmed().toInt(),
                            isMan = isMan()
                        )

                        navigateToGeneralTripFragment(userUid = args.userUid)
                    } else {
                        showToast(getString(R.string.all_fields_have_to_be_filled))
                    }
                }
            }
        }
    }

    private fun isMan(): Boolean = binding.radioButtonMan.isChecked

    private fun navigateToGeneralTripFragment(userUid: String) {
        UserProfileCreatingFragmentDirections
            .actionUserProfileCreatingFragmentToGeneralTripFragment(userUid = userUid)
            .also { findNavController().navigate(directions = it) }
    }

    private inline fun FragmentUserProfileCreatingBinding.verifyEditFields(
        block: (areFieldsValid: Boolean) -> Unit,
    ) {
        setOf(
            editTextName,
            editTextAge,
            editTextWeight,
            editTextHeight
        ).filter { it.toTrimmed().isNotEmpty() }
            .also { editTexts -> block(editTexts.isEmpty())}
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun EditText.toTrimmed(): String = text.toString().trim()
}