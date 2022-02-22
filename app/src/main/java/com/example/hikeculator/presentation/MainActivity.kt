package com.example.hikeculator.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}