package com.example.hikeculator.presentation.profile

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentProfileBinding
import com.example.hikeculator.domain.common.isEmptyWithoutSpaces
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender.MAN
import com.example.hikeculator.domain.enums.Gender.WOMAN
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.showSnackBar
import com.example.hikeculator.presentation.common.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel by viewModel<ProfileViewModel>()

    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectUserProfile()
        collectErrors()
        setOnSaveClickListener()
        setOnBackClickListener()
    }

    private fun collectUserProfile() {
        viewModel.user.collectWhenStarted(lifecycleScope) { user: User? ->
            user?.let { binding.setUserContent(it) }
        }
    }

    private fun collectErrors() {
        viewModel.errors.collectWhenStarted(lifecycleScope) { stringRes ->
            binding.root.showSnackBar(messageId = stringRes)
        }
    }

    private fun setOnSaveClickListener() {
        binding.buttonSave.setOnClickListener {
            if (chekAreFieldsCorrect()) {
                val gender = when (binding.radioGroupGender.checkedRadioButtonId) {
                    R.id.radio_button_woman -> WOMAN
                    else -> MAN
                }

                viewModel.updateUserProfile(
                    name = binding.editTextName.text.toString(),
                    weight = binding.editTextWeight.text.toString().toDouble(),
                    height = binding.editTextHeight.text.toString().toInt(),
                    age = binding.editTextAge.text.toString().toInt(),
                    gender = gender
                )

                requireContext().showToast(R.string.text_data_saving)
            } else {
                binding.root.showSnackBar(R.string.text_error_incorrect_fields)
            }
        }
    }

    private fun setOnBackClickListener() {
        binding.imageBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun chekAreFieldsCorrect(): Boolean {
        val weightIsNotEmpty = binding.editTextWeight.text.isNotEmptyWithoutSpaces()
        val heightIsNotEmpty = binding.editTextHeight.text.isNotEmptyWithoutSpaces()
        val ageIsNotEmpty = binding.editTextAge.text.isNotEmptyWithoutSpaces()
        val nameIsNotEmpty = binding.editTextName.text.isNotEmptyWithoutSpaces()

        return weightIsNotEmpty && heightIsNotEmpty && ageIsNotEmpty && nameIsNotEmpty
    }

    private fun FragmentProfileBinding.setUserContent(user: User) {
        editTextName.setText(user.name)
        editTextWeight.setText(user.weight.toString())
        editTextHeight.setText(user.height.toString())
        editTextAge.setText(user.age.toString())

        textViewCaloriesNorm.text = getString(R.string.format_kcal_long, user.calorieNorm)

        when (user.gender) {
            MAN -> radioButtonMan.isChecked = true
            WOMAN -> radioButtonWoman.isChecked = true
        }
    }

    private fun Editable?.isNotEmptyWithoutSpaces(): Boolean = !this.toString().isEmptyWithoutSpaces()
}