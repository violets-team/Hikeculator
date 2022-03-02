package com.example.hikeculator.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentEntranceBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class EntranceFragment : Fragment(R.layout.fragment_entrance) {

    private val binding by viewBinding(FragmentEntranceBinding::bind)
    private val viewModel by viewModel<EntranceViewModel>()


    private val entranceLauncher = this.registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { authenticationResult -> onSignInResult(authenticationResult) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (viewModel.isCurrentUserSignedIn()) {
            val userUid = viewModel.getUserUid()
            if (userUid != null) {
//                TODO("Navigate to  fragment and pass userUid")
            } else {
                TODO("Notify that the user isn't authenticated")
            }
        } else {
            entranceLauncher.launch(getSignInIntent())
        }
    }

    private fun onSignInResult(authenticationResult: FirebaseAuthUIAuthenticationResult) {
        if (authenticationResult.resultCode == AppCompatActivity.RESULT_OK) {
            val userUid = viewModel.getSignedInUserUid()
            if (userUid != null) {
                TODO("Navigate to profile creating fragment and pass userUid")
            } else {
                TODO("Notify that something went wrong")
            }
        } else {
            val response = authenticationResult.idpResponse
            /** Sign in failed. If response is null the user canceled the sign-in flow using the
             * back button. Otherwise check response.getError().getErrorCode() and handle the error.
             */
            TODO("Handle this case")
        }
    }

    private fun getSignInIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
//            .setLogo(R.drawable.my_great_logo) // Set logo drawable
//            .setTheme(R.style.MySuperAppTheme) // Set theme
            .setAvailableProviders(getAuthenticationProviders())
            .build()
    }

    private fun getAuthenticationProviders(): List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
//            AuthUI.IdpConfig.PhoneBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build(),
//            AuthUI.IdpConfig.TwitterBuilder().build()
    )
}