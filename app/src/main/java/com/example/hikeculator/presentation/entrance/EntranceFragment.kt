package com.example.hikeculator.presentation.entrance

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.MealType
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EntranceFragment : Fragment(R.layout.fragment_entrance) {

    private val navController by lazy { findNavController() }

    private val viewModel by viewModel<EntranceViewModel>()

    private val entranceLauncher = this.registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { authenticationResult -> onSignInResult(authenticationResult) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isCurrentUserSignedIn()) {
            val userUid = viewModel.getUserUid()
            val userEmail = viewModel.getUserEmail()

            if (userUid != null && userEmail != null) {
                viewModel.saveUserUid(uid = userUid)

                lifecycleScope.launch {
                    navigateByUserState(userUid = userUid, userEmail = userEmail)
                }
            } else {
                TODO("Notify that the user isn't authenticated")
            }
        } else {
            entranceLauncher.launch(getSignInIntent())
        }
    }

    private fun onSignInResult(authenticationResult: FirebaseAuthUIAuthenticationResult) {
        if (isResultOk(result = authenticationResult)) {
            val userUid = viewModel.getSignedInUserUid()
            val userEmail = viewModel.getSignedInUserEmail()

            if (userUid != null && userEmail != null) {
                viewModel.saveUserUid(uid = userUid)

                navigateToUserProfileCreatingFragment(uid = userUid, email = userEmail)
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
            .setAvailableProviders(getAuthenticationProviders())
            .build()
    }

    private fun getAuthenticationProviders(): List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    private fun isResultOk(result: FirebaseAuthUIAuthenticationResult): Boolean {
        return result.resultCode == AppCompatActivity.RESULT_OK
    }

    private suspend fun navigateByUserState(userUid: String, userEmail: String) {
        if (viewModel.isUserCreated(userUid = userUid)) {
            navigateToGeneralTripFragment(userUid = userUid)
        } else {
            navigateToUserProfileCreatingFragment(uid = userUid, email = userEmail)
        }
    }

    private fun navigateToGeneralTripFragment(userUid: String) {
        EntranceFragmentDirections.actionEntranceFragmentToGeneralTripFragment(
            userUid = userUid
        ).also { navController.navigate(directions = it) }
    }

    private fun navigateToUserProfileCreatingFragment(uid: String, email: String) {
        EntranceFragmentDirections.actionEntranceFragmentToUserProfileCreatingFragment(
            userUid = uid,
            userEmail = email
        ).also { navController.navigate(directions = it) }
    }
}