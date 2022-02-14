package com.example.hikeculator.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentSignInBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { authenticationResult ->
            onSignInResult(authenticationResult)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val user = Firebase.auth.currentUser

        if (user == null) {
            signInLauncher.launch(getSignInIntent())
        } else {
            binding.textView.text = "you're signed in already"
        }
    }

    private fun onSignInResult(authenticationResult: FirebaseAuthUIAuthenticationResult) {
        val response = authenticationResult.idpResponse

        if (authenticationResult.resultCode == AppCompatActivity.RESULT_OK) {
            binding.textView.text = "you've signed in successfully"
        } else {
            binding.textView.text = "error"
        }
    }

    private fun getSignInIntent(): Intent {
        return AuthUI.getInstance().createSignInIntentBuilder()
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